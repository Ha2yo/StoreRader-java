package com.storerader.server.domain.auth.dto;

public record TokenRefreshRequest(
        String refreshToken
) {}