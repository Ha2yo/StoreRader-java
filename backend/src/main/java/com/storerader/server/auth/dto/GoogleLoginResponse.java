package com.storerader.server.auth.dto;

public record GoogleLoginResponse(
        String accessToken,
        String refreshToken,
        UserResponse user,
        GoogleClaims claims
) {

    public record UserResponse(
            long id,
            String name,
            String email,
            String role
    ) {}
}