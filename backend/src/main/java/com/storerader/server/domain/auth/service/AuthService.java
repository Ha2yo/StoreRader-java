package com.storerader.server.domain.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.storerader.server.common.repository.sql.UserPreferenceSQL;
import com.storerader.server.domain.auth.model.GoogleIdTokenClaims;
import com.storerader.server.common.entity.UserEntity;
import com.storerader.server.common.repository.UserRepository;
import com.storerader.server.domain.auth.model.GoogleLoginResult;
import com.storerader.server.domain.auth.dto.request.GoogleLoginReq;
import com.storerader.server.domain.auth.dto.response.GoogleLoginRes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
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
    private final UserPreferenceSQL userPreferencesSQL;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;
    @Value("${JWT_SECRET}")
    private String secretKey;

    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7일

    /**
     * Google OAuth 로그인 전체 흐름을 처리한다.
     *
     * @param req Google ID Token을 포함한 로그인 요청
     * @return 서버 토큰(Access/Refresh) 및 로그인 유저 정보
     */
    @Transactional
    public GoogleLoginResult googleLogin(
            GoogleLoginReq req
    ) {
        // 구글 ID Token 검증
        GoogleIdTokenClaims claims = verifyGoogleIdToken(req.idToken(), googleClientId);

        // DB 유저 정보 업데이트
        UserEntity user = insertOrUpdateUser(claims);

        // DB user_preference 테이블 레코드 추가 (없는 경우)
        userPreferencesSQL.createDefaultPreference(user.getId());

        //  서버 자체 Token 생성
        String accessToken = createAccessToken(user);
        String refreshToken = createAndSaveRefreshToken(user);

        GoogleLoginResult.AuthUser userResponse = new GoogleLoginResult.AuthUser(
                user.getName(),
                user.getEmail(),
                user.getPicture(),
                user.getRole()
        );

        return new GoogleLoginResult(accessToken, refreshToken, userResponse);
    }

    /**
     * Google ID Token의 유효성을 검증한다. (RS256)
     *
     * @param idToken  Google에서 발급한 ID Token 문자열
     * @param clientId 우리 서비스의 Google OAuth Client ID
     * @return 검증된 토큰의 클레임 (식별자/이메일/이름/프로필 등)
     */
    public GoogleIdTokenClaims verifyGoogleIdToken(
            String idToken,
            String clientId
    ) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
                .setAudience(Collections.singletonList(clientId))
                .build();

        try {
            GoogleIdToken token = verifier.verify(idToken);

            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();

                return GoogleIdTokenClaims.from(token.getPayload());
            } else {
                throw new IllegalArgumentException("유효하지 않은 ID Token입니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("구글 토큰 검증 실패", e);
        }
    }

    /**
     * Google 클레임 정보를 기준으로 사용자 데이터를 갱신한다.
     *
     * @param claims Google ID Token에서 추출된 사용자 정보
     * @return 갱신된 유저 엔티티
     */
    @Transactional
    public UserEntity insertOrUpdateUser(
            GoogleIdTokenClaims claims
    ) {
        return userRepository.findBySub(claims.sub())
                .map(user -> {
                    // 기존 유저 -> 로그인 시각과 이름, 프로필사진 URL 업데이트
                    user.setLastLogin(OffsetDateTime.now());

                    user.setName(claims.name());
                    user.setPicture(claims.picture());

                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    // 신규 유저 -> 회원가입 처리
                    UserEntity newUser = new UserEntity();
                    newUser.setSub(claims.sub());
                    newUser.setEmail(claims.email());
                    newUser.setName(claims.name());
                    newUser.setPicture(claims.picture());
                    newUser.setCreatedAt(OffsetDateTime.now());

                    return userRepository.save(newUser);
                });
    }

    /**
     * JWT를 생성한다. (HS256)
     *
     * @param userId     유저 아이디
     * @param email      유저 이메일
     * @param role       유저 권한
     * @param expiration 만료 시각
     * @return 서명된 JWT 문자열
     */
    public String createToken(
            Long userId,
            String email,
            String role,
            long expiration
    ) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Access Token을 생성한다.
     *
     * @param user DB 상의 유저 엔티티
     * @return 생성된 Access Token
     */
    public String createAccessToken(
            @NonNull UserEntity user
    ) {
        return createToken(user.getId(), user.getEmail(), user.getRole(), ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * Refresh Token을 생성하고 DB에 저장한다.
     *
     * @param user DB 상의 유저 엔티티
     * @return 생성된 Refresh Token
     */
    @Transactional
    public String createAndSaveRefreshToken(
            @NonNull UserEntity user
    ) {
        String token = createToken(user.getId(), user.getEmail(), user.getRole(), REFRESH_TOKEN_EXPIRATION);

        user.setRefreshToken(token);
        user.setRefreshTokenExpiresAt(
                OffsetDateTime.now().plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRATION))
        );
        userRepository.save(user);

        return token;
    }

    /**
     * Refresh Token을 검증한 뒤 새로운 Access Token을 발급한다.
     *
     * @param refreshToken 클라이언트가 전달한 Refresh Token
     * @return 새로 발급된 Access Token
     */
    @Transactional
    public String refreshAccessToken(
            String refreshToken
    ) {
        // 토큰 복호화 및 검증
        Claims claims = decodeJwt(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());

        // DB에서 해당 유저 및 저장된 토큰 정보 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        // DB에 기록된 만료 시각 체크
        if (user.getRefreshTokenExpiresAt().isBefore(OffsetDateTime.now())) {
            throw new RuntimeException("Refresh Token이 만료되었습니다.");
        }

        // 새 Access Token 생성
        return createAccessToken(user);
    }

    /**
     * JWT 토큰의 서명을 확인하고 Claims를 반환한다.
     *
     * @param token 검증할 JWT 문자열
     * @return payload claims
     */
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

    /**
     * Access Token을 기반으로 현재 로그인한 유저 정보를 조회한다.
     *
     * @param accessToken Access Token 문자열
     * @return 유저 응답 DTO
     */
    public GoogleLoginRes.UserResponse getMyInfo(
            String accessToken
    ) {

        if (accessToken == null) {
            throw new RuntimeException("인증 토큰이 없습니다.");
        }

        try {
            // 토큰 해독 및 유저 ID 추출
            Claims claims = decodeJwt(accessToken);
            Long userId = Long.parseLong(claims.getSubject());

            // DB에서 유저 조회
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            return new GoogleLoginRes.UserResponse(
                    user.getName(),
                    user.getEmail(),
                    user.getPicture(),
                    user.getRole()
            );
        } catch (Exception e) {
            // 토큰이 만료되었거나 변조된 경우
            throw new RuntimeException("유효하지 않은 토큰입니다.", e);
        }
    }
}