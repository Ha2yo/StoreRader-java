package com.storerader.server.domain.admin.dto.select.prices;

import com.storerader.server.common.entity.PriceEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "가격 조회 응답")
public record Price(
        @Schema(description = "DB PK", example = "3488")
        int id,

        @Schema(description = "상품 ID", example = "1200")
        int goodId,

        @Schema(description = "매장 ID", example = "33")
        long storeId,

        @Schema(description = "가격 조사일자", example = "20260130")
        String inspectDay,

        @Schema(description = "상품 가격", example = "4200")
        Integer price,

        @Schema(description = "1+1 여부", example = "N")
        String isOnePlusOne,

        @Schema(description = "할인 여부", example = "Y")
        String isDiscount,

        @Schema(description = "할인 시작일자", example = "20260202")
        String discountStart,

        @Schema(description = "할인 종료일자", example = "20260216")
        String discountEnd,

        @Schema(description = "레코드 생성일시", example = "2026-02-09 18:56:21.173467+09")
        OffsetDateTime createdAt
) {
    public static Price from(PriceEntity priceEntity) {
        return new Price(
                priceEntity.getId(),
                priceEntity.getGoodId(),
                priceEntity.getStoreId(),
                priceEntity.getInspectDay(),
                priceEntity.getPrice(),
                priceEntity.getIsOnePlusOne(),
                priceEntity.getIsDiscount(),
                priceEntity.getDiscountStart(),
                priceEntity.getDiscountEnd(),
                priceEntity.getCreatedAt()
        );
    }
}
