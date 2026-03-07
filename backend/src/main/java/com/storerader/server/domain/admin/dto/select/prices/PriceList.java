package com.storerader.server.domain.admin.dto.select.prices;

import com.storerader.server.common.entity.PriceEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "가격 목록 조회 응답")
public record PriceList(
        @Schema(description = "가격 목록")
        List<Price> prices,

        @Schema(description = "전체 상품 개수")
        long totalCount,

        @Schema(description = "현재 페이지 번호")
        int page,

        @Schema(description = "페이지 크기")
        int size
) {
    public static PriceList from(Page<PriceEntity> pageResult) {
        List<Price> prices = pageResult
                .getContent()
                .stream()
                .map(Price::from)
                .toList();

        return new PriceList(
                prices,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }
}
