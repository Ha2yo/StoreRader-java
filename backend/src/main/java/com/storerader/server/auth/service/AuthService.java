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
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${JWT_SECRET}")
    private String secretKey;

    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24시간

    // 함수 0
    public GoogleLoginResponse authGoogle(
            GoogleLoginRequest req
    ) {
        // IDTOken 검증
        GoogleClaims claims = verifyGoogleIdToken(req.getIdToken(), googleClientId);

        // DB에 유저 정보 업데이트
        UserEntity user = insertOrUpdateUser(claims);

        // 유저 JWT 생성
        String serverJwt = createJwt(user.getId(), user.getEmail());

        // 응답 DTO 생성
        GoogleLoginResponse.UserResponse userResponse = new GoogleLoginResponse.UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                claims.getPicture()
        );

        return new GoogleLoginResponse(serverJwt, userResponse, claims);
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
    public UserEntity insertOrUpdateUser(GoogleClaims claims) {
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
    public String createJwt(
            Long userId,
            String email
    ) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
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
}
