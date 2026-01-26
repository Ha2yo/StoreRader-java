package com.storerader.server.auth.controller;

import com.storerader.server.auth.dto.GoogleLoginRequest;
import com.storerader.server.auth.dto.GoogleLoginResponse;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    @Value("${app.cookie.same-site:Lax}")
    private String cookieSameSite;

    private static final long ACCESS_TOKEN_MAX_AGE_SEC = 30 * 60;           // 30분
//    private static final long REFRESH_TOKEN_MAX_AGE_SEC = 7 * 24 * 60 * 60; // 7일
    private static final long REFRESH_TOKEN_MAX_AGE_SEC = 24 * 60 * 60; // 1일

    @PostMapping("/google")
    public ResponseEntity<GoogleLoginResponse> googleLogin(
            @RequestBody GoogleLoginRequest request,
            HttpServletResponse response
    ) {
        GoogleLoginResponse loginResponse = authService.authGoogle(request);

        addCookie(response, buildCookie("refreshToken", loginResponse.getRefreshToken(), REFRESH_TOKEN_MAX_AGE_SEC));
        addCookie(response, buildCookie("accessToken", loginResponse.getAccessToken(), ACCESS_TOKEN_MAX_AGE_SEC));

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken, // 쿠키에서 직접 읽기
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 서비스에서 새 토큰 생성
        String newAccessToken = authService.refreshAccessToken(refreshToken);

        addCookie(response, buildCookie("accessToken", newAccessToken, ACCESS_TOKEN_MAX_AGE_SEC));

        return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken));
    }

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response
    ) {
        addCookie(response, deleteCookie("accessToken"));
        addCookie(response, deleteCookie("refreshToken"));
        return ResponseEntity.ok().build();
    }

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

    private void addCookie(
            HttpServletResponse response,
            ResponseCookie cookie
        ) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}

