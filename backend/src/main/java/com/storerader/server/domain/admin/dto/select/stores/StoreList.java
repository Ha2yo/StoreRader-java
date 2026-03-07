package com.storerader.server.domain.admin.dto.select.stores;

import com.storerader.server.common.entity.StoreEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "매장 목록 조회 응답")
public record StoreList(
        List<Store> stores,

        @Schema(description = "전체 상품 개수")
        long totalCount,

        @Schema(description = "현재 페이지 번호")
        int page,

        @Schema(description = "페이지 크기")
        int size
) {
    public static StoreList from(Page<StoreEntity> pageResult) {
        List<Store> stores = pageResult
                .getContent()
                .stream()
                .map(Store::from)
                .toList();

        return new StoreList(
                stores,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }
}
