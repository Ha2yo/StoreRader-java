package com.storerader.server.config;

import com.storerader.server.common.web.cookie.CookieHelper;
import com.storerader.server.domain.admin.service.AdminAuthorizationService;
import com.storerader.server.domain.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final CookieHelper cookieHelper;
    private final AuthService authService;
    private final AdminAuthorizationService adminAuthorizationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = cookieHelper.extractTokenFromCookie(request, "accessToken");

        if (accessToken == null || accessToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증 토큰이 없습니다.");
        }

        Claims claims = authService.decodeJwt(accessToken);

        long userId;
        try {
            userId = Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
        }

        adminAuthorizationService.requireAdmin(userId);
        return true;
    }
}