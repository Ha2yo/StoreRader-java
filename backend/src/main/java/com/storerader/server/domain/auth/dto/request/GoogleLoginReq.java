package com.storerader.server.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Google Id Token을 이용한 로그인 요청")
public record GoogleLoginReq(
        @Schema(description = "Google OAuth에서 발급된 ID Token")
        String idToken
) {}