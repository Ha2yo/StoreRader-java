package com.storerader.server.domain.userSelectionLog.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserSelectionLogReq(
        @Schema(description = "선택한 매장 ID", example = "1766")
        Long storeId,

        @Schema(description = "선택한 상품 ID", example = "1381")
        Long goodId,

        @Schema(description = "선택 당시 상품 가격", example = "10980")
        Integer price,

        @Schema(description = "사용자 선호 유형 (price or distance)", example = "distance")
        String preferenceType
) {}