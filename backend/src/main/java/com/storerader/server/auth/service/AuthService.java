package com.storerader.server.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.storerader.server.auth.dto.GoogleClaims;
import com.storerader.server.auth.dto.GoogleLoginRequest;
import com.storerader.server.auth.dto.GoogleLoginResponse;
import com.storerader.server.common.entity.UserEntity;
import com.storerader.server.common.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${JWT_SECRET}")
    private String secretKey;

    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7일

    // 함수 0
    @Transactional
    public GoogleLoginResponse authGoogle(
            GoogleLoginRequest req
    ) {
        // 구글 ID Token 검증
        GoogleClaims claims = verifyGoogleIdToken(req.getIdToken(), googleClientId);

        // DB 유저 정보 업데이트
        UserEntity user = insertOrUpdateUser(claims);

        // Dual Token 생성
        String accessToken = createAccessToken(user.getId(), user.getEmail());
        String refreshToken = createAndSaveRefreshToken(user);

        // 응답 DTO 생성
        GoogleLoginResponse.UserResponse userResponse = new GoogleLoginResponse.UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                claims.getPicture()
        );

        return new GoogleLoginResponse(accessToken, refreshToken, userResponse, claims);
    }


    // 함수 1
    public GoogleClaims verifyGoogleIdToken(
            String idToken,
            String clientId
    ) {
        // 1. verifier 초기화
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
                .setAudience(Collections.singletonList(clientId))
                .build();

        try {
            // 2. 토큰 검증
            GoogleIdToken token = verifier.verify(idToken);

            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();

                // 3. 데이터를 DTO에 매핑

                return GoogleClaims.from(token.getPayload());
            } else {
                throw new IllegalArgumentException("유효하지 않은 ID Token입니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("구글 토큰 검증 실패", e);
        }
    }

    // 함수 2
    @Transactional
    public UserEntity insertOrUpdateUser(
            GoogleClaims claims
    ) {
        return userRepository.findBySub(claims.getSub())
                .map(user -> {
                    // 유저가 있는 경우
                    user.setLastLogin(OffsetDateTime.now());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    // 유저가 없는 경우
                    UserEntity newUser = new UserEntity();
                    newUser.setSub(claims.getSub());
                    newUser.setEmail(claims.getEmail());
                    newUser.setName(claims.getName());
                    newUser.setCreatedAt(OffsetDateTime.now());
                    return userRepository.save(newUser);
                });
    }

    // 함수 3
    public String createToken(
            Long userId,
            String email,
            long expiration
    ) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String createAccessToken(
            Long userId,
            String email
    ){
        return createToken(userId, email, ACCESS_TOKEN_EXPIRATION);
    }

    @Transactional
    public String createAndSaveRefreshToken(
            UserEntity user
    ) {
        String token = createToken(user.getId(), user.getEmail(), REFRESH_TOKEN_EXPIRATION);

        user.setRefreshToken(token);
        user.setRefreshTokenExpiresAt(
                OffsetDateTime.now().plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRATION))
        );
        userRepository.save(user);

        return token;
    }

    @Transactional
    public String refreshAccessToken(
            String refreshToken
    ) {
        // 토큰 복호화 및 검증
        Claims claims = decodeJwt(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());

        // DB에서 유저 조회 및 토큰 일치 여부 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if(user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        // DB에 기록된 만료 시각 체크
        if (user.getRefreshTokenExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new RuntimeException("Refresh Token이 만료되었습니다.");
        }

        // 새 Access Token 발급
        return createAccessToken(user.getId(), user.getEmail());
    }

    // 함수 4
    public Claims decodeJwt(
            String token
    ) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public GoogleLoginResponse.UserResponse getCurrentUserInfo(
            HttpServletRequest request
    ) {
        // 1. 쿠키에서 accessToken 추출
        String accessToken = extractTokenFromCookie(request, "accessToken");

        if (accessToken == null) {
            throw new RuntimeException("인증 토큰이 없습니다.");
        }

        try {
            // 2. 토큰 해독 및 유저 ID 추출
            Claims claims = decodeJwt(accessToken);
            Long userId = Long.parseLong(claims.getSubject());

            // 3. DB에서 유저 조회
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            // 4. DTO 반환
            return new GoogleLoginResponse.UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    null // 필요한 경우 프로필 이미지 URL 필드를 엔티티에 추가하여 전달
            );
        } catch (Exception e) {
            // 토큰이 만료되었거나 변조된 경우
            throw new RuntimeException("유효하지 않은 토큰입니다.", e);
        }
    }


    private String extractTokenFromCookie(
            HttpServletRequest request,
            String name
    ) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}