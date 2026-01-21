package com.storerader.server.auth.controller;

import com.storerader.server.auth.dto.GoogleLoginRequest;
import com.storerader.server.auth.dto.GoogleLoginResponse;
import com.storerader.server.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
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
    public GoogleLoginResponse googleLogin(@RequestBody GoogleLoginRequest request) {
        return authService.authGoogle(request);
    }
}