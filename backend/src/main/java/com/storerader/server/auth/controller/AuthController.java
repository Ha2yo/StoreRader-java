package com.storerader.server.auth.controller;

import com.storerader.server.auth.dto.GoogleLoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/auth/google")
    public boolean googleLogin(@RequestBody GoogleLoginRequest req) {
        return req.getIdToken() != null && !req.getIdToken().isEmpty();
    }
}