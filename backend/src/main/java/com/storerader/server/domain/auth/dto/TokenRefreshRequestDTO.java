package com.storerader.server.domain.auth.dto;

public record TokenRefreshRequestDTO(
        String refreshToken
) {}