package com.storerader.server.auth.dto;

public record TokenRefreshRequest(
        String refreshToken
) {}