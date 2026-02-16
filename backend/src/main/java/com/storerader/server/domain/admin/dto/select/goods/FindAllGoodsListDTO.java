package com.storerader.server.domain.admin.dto.select.goods;

import com.storerader.server.common.entity.GoodEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public record FindAllGoodsListDTO(
        List<FindAllGoodsDTO> goods,
        long totalCount,
        int page,
        int size
) {
    public static FindAllGoodsListDTO from(Page<GoodEntity> pageResult) {
        List<FindAllGoodsDTO> goods = pageResult
                .getContent()
                .stream()
                .map(FindAllGoodsDTO::from)
                .toList();

        return new FindAllGoodsListDTO(
                goods,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }
}