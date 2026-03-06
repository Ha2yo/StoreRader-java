package com.storerader.server.domain.good.dto.res;

import com.storerader.server.common.entity.GoodEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 정보 응답")
public record GoodRes(
        @Schema(description = "DB PK", example = "1")
        Long id,

        @Schema(description = "상품 ID", example = "35")
        Integer goodId,

        @Schema(description = "상품 이름", example = "해표 꽃소금 (1kg)")
        String goodName,

        @Schema(description = "상품 용량", example = "1000")
        Number totalCnt,

        @Schema(description = "상품 구분코드", example = "G")
        String totalDivCode
) {
    public static GoodRes from(GoodEntity g) {
        return new GoodRes(
                g.getId(),
                g.getGoodId(),
                g.getGoodName(),
                g.getTotalCnt(),
                g.getTotalDivCode()
        );
    }
}