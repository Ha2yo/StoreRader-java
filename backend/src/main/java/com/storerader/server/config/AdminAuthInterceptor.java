package com.storerader.server.config;

import com.storerader.server.common.exception.CustomException;
import com.storerader.server.common.exception.ExceptionClass;
import com.storerader.server.common.repository.UserRepository;
import com.storerader.server.common.web.cookie.CookieHelper;
import com.storerader.server.domain.auth.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final CookieHelper cookieHelper;
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = cookieHelper.extractTokenFromCookie(request, "accessToken");

        if (accessToken == null || accessToken.isBlank()) {
            throw new CustomException(ExceptionClass.UNAUTHORIZED);
        }

        Claims claims = authService.decodeJwt(accessToken);

        String role = claims.get("role", String.class);

        if (!"ADMIN".equals(role)) {
            throw new CustomException(ExceptionClass.FORBIDDEN);
        }

        return true;
    }
}