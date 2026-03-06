package com.storerader.server.domain.price.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가격 데이터 응답")
public record PriceItemDTO(
        @Schema(description = "매장 ID", example = "3")
        Long storeId,

        @Schema(description = "상품 가격", example = "5980")
        Integer price,

        @Schema(description = "가격 조사일자", example = "20260130")
        String inspectDay
) {}