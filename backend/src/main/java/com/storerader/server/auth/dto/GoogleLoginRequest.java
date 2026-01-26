package com.storerader.server.auth.dto;

public record GoogleLoginRequest (
        String idToken  // Google이 발급한 IDToken
) {}