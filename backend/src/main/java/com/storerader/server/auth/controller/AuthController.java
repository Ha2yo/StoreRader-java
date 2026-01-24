package com.storerader.server.auth.controller;

import com.storerader.server.auth.dto.GoogleLoginRequest;
import com.storerader.server.auth.dto.GoogleLoginResponse;
import com.storerader.server.auth.dto.TokenRefreshRequest;
import com.storerader.server.auth.dto.TokenRefreshResponse;
import com.storerader.server.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/google")
    public GoogleLoginResponse googleLogin(
            @RequestBody GoogleLoginRequest request,
            HttpServletResponse response
    ) {
        GoogleLoginResponse loginResponse = authService.authGoogle(request);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", loginResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7*24*60*60)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", loginResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30*60)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", accessTokenCookie.toString());

        return loginResponse;

        return authService.authGoogle(request);
    }

    @PostMapping("/refresh")
    public TokenRefreshResponse refresh(@RequestBody TokenRefreshRequest request) {
        String newAccessToken = authService.refreshAccessToken(request.getRefreshToken());

        return new TokenRefreshResponse(newAccessToken);
    }
}