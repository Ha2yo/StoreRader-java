package com.storerader.server.domain.admin.dto.select.goods;

import com.storerader.server.common.entity.GoodEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "상품 목록 조회 응답")
public record GoodList(
        @Schema(description = "상품 목록")
        List<Good> goods,

        @Schema(description = "전체 상품 개수")
        long totalCount,

        @Schema(description = "현재 페이지 번호")
        int page,

        @Schema(description = "페이지 크기")
        int size
) {
    public static GoodList from(Page<GoodEntity> pageResult) {
        List<Good> goods = pageResult
                .getContent()
                .stream()
                .map(Good::from)
                .toList();

        return new GoodList(
                goods,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }
}