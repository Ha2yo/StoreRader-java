/*
 * File: domain/auth/service/AuthService.java
 * Description:
 *     인증(auth) 도메인의 서비스 계층으로,
 *     Google Oauth 인증 처리 및 JWT 기반 인증 토큰 관리를 담당한다
 *
 * Responsibilities:
 *      1) googleLogin()
 *
 *      2) verifyGoogleIdToken()
 *
 *      3) insertOrUpdateUser()
 *
 *      4) createAccessToken()
 *
 *      5) createAndSaveRefreshToken()
 *
 *      6) decodeJwt()
 *
 *      7) getMyInfo()

 */

package com.storerader.server.domain.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.storerader.server.domain.auth.dto.GoogleClaimsDTO;
import com.storerader.server.domain.auth.dto.GoogleLoginRequestDTO;
import com.storerader.server.domain.auth.dto.GoogleLoginResponseDTO;
import com.storerader.server.common.entity.UserEntity;
import com.storerader.server.common.repository.UserRepository;
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

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;
    @Value("${JWT_SECRET}")
    private String secretKey;

    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000; // 30분
    //    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7일
    private static final long REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000; // 24시간

    /**
     * Google OAuth 로그인 전체 흐름을 관리한다.
     *
     * @param req Google에서 발급한 IDToken
     * @return ㅇ
     */
    @Transactional
    public GoogleLoginResponseDTO googleLogin(
            GoogleLoginRequestDTO req
    ) {
        // 1. 구글 ID Token 검증
        GoogleClaimsDTO claims = verifyGoogleIdToken(req.idToken(), googleClientId);

        // 2. DB 유저 정보 업데이트
        UserEntity user = insertOrUpdateUser(claims);

        // 3. 서버 자체 Token 생성
        String accessToken = createAccessToken(user);
        String refreshToken = createAndSaveRefreshToken(user);

        // 응답 DTO 생성
        GoogleLoginResponseDTO.UserResponse userResponse = new GoogleLoginResponseDTO.UserResponse(
                user.getName(),
                user.getEmail(),
                claims.picture(),
                user.getRole()
        );

        return new GoogleLoginResponseDTO(accessToken, refreshToken, userResponse, claims);
    }

    /**
     * Google ID Token이 유효한지 검증한다 (RS256)
     *
     * @param idToken  Google에서 발급한 ID Token
     * @param clientId 우리 사이트 전용 clientId
     * @return Google ID Token으로부터 얻어낸 유저 정보 (sub, email, name..)
     */
    public GoogleClaimsDTO verifyGoogleIdToken(
            String idToken,
            String clientId
    ) {
        // verifier 초기화
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
                .setAudience(Collections.singletonList(clientId))
                .build();

        try {
            // 토큰 검증
            GoogleIdToken token = verifier.verify(idToken);

            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();

                // 데이터를 DTO에 매핑
                return GoogleClaimsDTO.from(token.getPayload());
            } else {
                throw new IllegalArgumentException("유효하지 않은 ID Token입니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException("구글 토큰 검증 실패", e);
        }
    }

    /**
     * 유저 정보를 DB에 업데이트한다.
     *
     * @param claims Google ID Token으로부터 얻어낸 유저 정보 (sub, email, name..)
     * @return 업데이트한 유저의 정보
     */
    @Transactional
    public UserEntity insertOrUpdateUser(
            GoogleClaimsDTO claims
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
     * 유저의 정보를 바탕으로 토큰을 생성한다. (HS256)
     *
     * @param userId     유저 아이디
     * @param email      유저 이메일
     * @param role       유저 권한
     * @param expiration 만료 시각
     * @return 생성된 토큰
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
     * 액세스 토큰을 생성한다.
     *
     * @param user DB 상의 유저 정보
     * @return 생성된 액세스 토큰
     */
    public String createAccessToken(
            @NonNull UserEntity user
    ) {
        return createToken(user.getId(), user.getEmail(), user.getRole(), ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * 리프레시 토큰을 생성하고 DB에 저장한다.
     *
     * @param user DB 상의 유저 정보
     * @return 생성된 리프레시 토큰
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
     * DB에 저장된 리프레시 토큰으로 새로운 액세스 토큰을 발급한다.
     *
     * @param refreshToken 클라이언트로부터 전달받은 리프레시 토큰
     * @return 갱신된 새로운 리프레시 토큰
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
     * JWT 토큰의 서명을 확인하고 내부 페이로드를 해독한다.
     *
     * @param token 해독할 JWT 토큰 문자열
     * @return 토큰에 담긴 페이로드 정보
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
     * HTTP 요청의 쿠키에서 Access Token을 추출하여 현재 로그인한 유저의 정보를 조회한다.
     *
     * @param accessToken 액세스 토큰
     * @return 유저 정보
     */
    public GoogleLoginResponseDTO.UserResponse getMyInfo(
            String accessToken
    ) {

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
            return new GoogleLoginResponseDTO.UserResponse(
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