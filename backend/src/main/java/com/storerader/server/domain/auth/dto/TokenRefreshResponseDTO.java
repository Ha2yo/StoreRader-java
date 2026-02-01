package com.storerader.server.domain.auth.dto;

public record TokenRefreshResponse(
        String accessToken
) {}