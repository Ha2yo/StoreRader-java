package com.storerader.server.domain.admin.dto.select.goods;
import com.storerader.server.common.entity.GoodEntity;

import java.time.OffsetDateTime;

public record FindAllGoodsDTO(
        Long id,
        int goodId,
        String goodName,
        int totalCnt,
        String totalDivCode,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static FindAllGoodsDTO from(GoodEntity goodEntity) {
        return new FindAllGoodsDTO(
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