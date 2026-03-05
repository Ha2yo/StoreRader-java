package com.storerader.server.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Access Token 재발급 응답")
public record RefreshAccessTokenRes(
        @Schema(description = "새로 발급된 Access Token")
        String accessToken
) {}