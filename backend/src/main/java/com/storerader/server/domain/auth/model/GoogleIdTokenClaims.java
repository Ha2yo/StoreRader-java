package com.storerader.server.domain.auth.model;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public record GoogleIdTokenClaims(
        String iss,             // 토큰 발급자
        String sub,             // Google 유저 고유 ID
        String aud,             // 토큰 수신자
        String email,           // 유저 이메일
        Boolean emailVerified,  // 이메일 인증 여부
        Long exp,               // 토큰 만료 시각
        Long iat,               // 토큰 발급 시각
        String name,            // 유저 이름
        String picture          // 프로필 이미지 URL
) {

    public static GoogleIdTokenClaims from(GoogleIdToken.Payload payload) {
        return new GoogleIdTokenClaims(
                payload.getIssuer(),
                payload.getSubject(),
                String.valueOf(payload.getAudience()),
                payload.getEmail(),
                payload.getEmailVerified(),
                payload.getExpirationTimeSeconds(),
                payload.getIssuedAtTimeSeconds(),
                String.valueOf(payload.get("name")),
                String.valueOf(payload.get("picture"))
        );
    }
}