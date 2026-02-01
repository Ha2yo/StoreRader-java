package com.storerader.server.domain.auth.dto;

public record GoogleLoginRequestDTO(
        String idToken  // Google이 발급한 IDToken
) {}