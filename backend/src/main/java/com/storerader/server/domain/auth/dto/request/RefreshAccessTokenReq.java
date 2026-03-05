package com.storerader.server.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Access Token 재발급 요청")
public record RefreshAccessTokenReq(
        @Schema(description = "재발급을 위해 사용하는 Refesh Token")
        String refreshToken
) {}