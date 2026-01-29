package com.storerader.server.config;

import com.storerader.server.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {
    private final AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("[ADMIN-INTERCEPTOR] uri=" + request.getRequestURI()
                + " servletPath=" + request.getServletPath()
                + " contextPath=" + request.getContextPath()
                + " method=" + request.getMethod());
        adminService.requireAdminFromDb(request);
        return true;
    }
}
