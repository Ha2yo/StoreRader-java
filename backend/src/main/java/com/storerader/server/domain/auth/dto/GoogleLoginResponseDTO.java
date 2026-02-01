package com.storerader.server.domain.auth.dto;

public record GoogleLoginResponseDTO(
        String accessToken,
        String refreshToken,
        UserResponse user,
        GoogleClaimsDTO claims
) {

    public record UserResponse(
            String name,
            String email,
            String picture,
            String role
    ) {}
}