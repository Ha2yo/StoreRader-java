package com.storerader.server.auth.dto;

public record GoogleLoginResponse(
        String accessToken,
        String refreshToken,
        UserResponse user,
        GoogleClaims claims
) {

    public record UserResponse(
            String name,
            String email,
            String picture
    ) {}
}