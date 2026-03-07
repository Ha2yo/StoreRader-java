package com.storerader.server.domain.admin.dto.select.goods;

import com.storerader.server.common.entity.GoodEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "상품 조회 응답")
public record Good(
        @Schema(description = "DB PK", example = "1")
        Long id,

        @Schema(description = "상품 ID", example = "35")
        int goodId,

        @Schema(description = "상품 이름", example = "해표 꽃소금 (1kg)")
        String goodName,

        @Schema(description = "상품 용량/수량", example = "1000")
        int totalCnt,

        @Schema(description = "상품 구분 코드", example = "G")
        String totalDivCode,

        @Schema(description = "레코드 생성일시", example = "2026-02-03 02:34:24.043086+09")
        OffsetDateTime createdAt,

        @Schema(description = "레코드 수정일시", example = " 2026-02-03 22:10:07.76093+09")
        OffsetDateTime updatedAt
) {
    public static Good from(GoodEntity goodEntity) {
        return new Good(
                goodEntity.getId(),
                goodEntity.getGoodId(),
                goodEntity.getGoodName(),
                goodEntity.getTotalCnt(),
                goodEntity.getTotalDivCode(),
                goodEntity.getCreatedAt(),
                goodEntity.getUpdatedAt()
        );
    }
}