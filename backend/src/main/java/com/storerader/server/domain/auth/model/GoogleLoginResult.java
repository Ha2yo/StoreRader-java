package com.storerader.server.domain.auth.model;

public record GoogleLoginResult(
        String accessToken,     // 발급된 Access Token
        String refreshToken,    // 발급된 Refresh Token
        AuthUser user           // 인증된 유저 정보
) {
    public record AuthUser(
            String name,    // 유저 이름
            String email,   // 유저 이메일
            String picture, // 프로필 이미지 URL
            String role     // 유저 권한
    ) {}
}