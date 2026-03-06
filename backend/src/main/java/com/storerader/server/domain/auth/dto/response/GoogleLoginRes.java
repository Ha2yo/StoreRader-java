package com.storerader.server.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Google OAuth 로그인 응답")
public record GoogleLoginRes(
        @Schema(description = "로그인한 유저의 정보")
        UserResponse user
) {

    @Schema(description = "로그인한 유저 정보")
    public record UserResponse(
            @Schema(description = "유저 이름", example = "조장현")
            String name,

            @Schema(description = "유저 이메일", example = "ha2yo1114@gmail.com")
            String email,

            @Schema(description = "유저 프로필 이미지 URL", example = "https://lh3.googleusercontent.com/...")
            String picture,

            @Schema(description = "유저 권한", example = "USER")
            String role 
    ) {}
}