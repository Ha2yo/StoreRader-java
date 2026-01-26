package com.storerader.server.auth.controller;

import com.storerader.server.auth.dto.GoogleLoginRequest;
import com.storerader.server.auth.dto.GoogleLoginResponse;
import com.storerader.server.auth.dto.TokenRefreshRequest;
import com.storerader.server.auth.dto.TokenRefreshResponse;
import com.storerader.server.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 구글 OAuth2.0 기반 인증 및 JWT 토큰 관리 컨트롤러
 * - 클라이언트의 구글 로그인 요청 처리
 * - JWT Access/Refresh 토큰 발급 및 쿠키 저장
 * - 토큰 갱신 및 로그아웃 관리
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 쿠키 보안 설정
    @Value("${APP_COOKIE_SECURE}")
    private boolean cookieSecure;   // false
    @Value("${APP_COOKIE_SAME_SITE}")
    private String cookieSameSite;  // Lax

    private static final long ACCESS_TOKEN_EXPIRATION = 30 * 60;           // 30분
//    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60; // 7일
    private static final long REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60; // 1일

    /**
     * 구글 ID Token으로 로그인을 시도한다.
     * 성공 시 Access/Refresh Token을 응답 헤더의 쿠키에 설정한다.
     * @param request   Google ID Token
     * @param response  헤더를 추가하기 위한 응답 객체
     * @return 로그인 성공 유저 정보 및 토큰 정보
     */
    @PostMapping("/google")
    public ResponseEntity<GoogleLoginResponse> googleLogin(
            @RequestBody GoogleLoginRequest request,
            HttpServletResponse response
    ) {
        GoogleLoginResponse loginResponse = authService.authGoogle(request);

        addCookie(
                response, buildCookie(
                        "refreshToken", loginResponse.refreshToken(), REFRESH_TOKEN_EXPIRATION
                )
        );
        addCookie(
                response, buildCookie(
                        "accessToken", loginResponse.accessToken(), ACCESS_TOKEN_EXPIRATION
                )
        );

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받는다.
     * @param tokenRefreshRequest  쿠키에서 추출한 리프레시 토큰
     * @param response      새 액세스 토큰 쿠키 설정을 위한 응답 객체
     * @return 새로 발급된 액세스 토큰
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(
            TokenRefreshRequest tokenRefreshRequest, // 쿠키에서 직접 읽기
            HttpServletResponse response
    ) {
        if (tokenRefreshRequest == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 서비스에서 새 토큰 생성
        String newAccessToken = authService.refreshAccessToken(tokenRefreshRequest.toString());

        addCookie(response, buildCookie("accessToken", newAccessToken, ACCESS_TOKEN_EXPIRATION));

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken));
    }

    /**
     * 현재 로그인한 유저의 정보를 조회한다.
     * @param request 현재 요청 객체
     * @return 로그인한 유저의 정보 (id, name, email..)
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(
            HttpServletRequest request
    ) {
        try {
            return ResponseEntity.ok(authService.getCurrentUserInfo(request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * 로그아웃을 처리한다.
     * @param response 쿠키 삭제 헤더를 추가하기 위한 응답 객체
     * @return 성공 시 OK
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response
    ) {
        addCookie(response, deleteCookie("accessToken"));
        addCookie(response, deleteCookie("refreshToken"));
        return ResponseEntity.ok().build();
    }

    /**
     * 보안 정책이 적용된 쿠키를 생성한다.
     * @param name 쿠키의 이름
     * @param value 쿠키에 저장할 값
     * @param maxAgeSec 쿠키 유효 기간
     * @return 생성된 쿠키
     */
    private ResponseCookie buildCookie(
            String name,
            String value,
            long maxAgeSec
    ) {
        boolean secure = cookieSecure;
        String sameSite = cookieSameSite;

        if ("None".equalsIgnoreCase(sameSite)) {
            secure = true;
        }

        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(maxAgeSec)
                .sameSite(sameSite)
                .build();
    }

    /**
     * 쿠키를 삭제하기 위한 만료된 쿠키 객체를 생성한다.
     * @param name 삭제할 쿠키의 이름
     * @return 만료된 쿠키
     */
    private ResponseCookie deleteCookie(
            String name
    ) {
        boolean secure = cookieSecure;
        String sameSite = cookieSameSite;

        if ("None".equalsIgnoreCase(sameSite)) {
            secure = true;
        }

        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(0)
                .sameSite(sameSite)
                .build();
    }

    /**
     * 최종 생성된 쿠키를 HTTP 응답 헤더에 추가한다.
     * @param response 응답 객체
     * @param cookie   추가할 쿠키
     */
    private void addCookie(
            HttpServletResponse response,
            ResponseCookie cookie
        ) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
