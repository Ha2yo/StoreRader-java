package com.storerader.server.auth.dto;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public record GoogleClaims(
        String iss,             // 토큰 발급자
        String sub,             // Google 유저 고유 ID
        String aud,             // 토큰 수신자
        String email,           // 유저 이메일
        Boolean emailVerified,  // 이메일 인증 여부 (카멜케이스로 변경)
        Long exp,               // 만료 시각
        Long iat,               // 발급 시각
        String name,            // 유저 이름
        String picture          // 유저 프로필 이미지 URL
) {

    public static GoogleClaims from(GoogleIdToken.Payload payload) {
        return new GoogleClaims(
                payload.getIssuer(),
                payload.getSubject(),
                payload.getAudience().toString(),
                payload.getEmail(),
                payload.getEmailVerified(),
                payload.getExpirationTimeSeconds(),
                payload.getIssuedAtTimeSeconds(),
                String.valueOf(payload.get("name")),
                String.valueOf(payload.get("picture"))
        );
    }
}