package com.storerader.server.domain.good.dto;

import com.storerader.server.common.entity.GoodEntity;

import java.time.OffsetDateTime;

public record GoodItemDTO(
        Long id,
        Integer goodId,
        String goodName,
        Number totalCnt,
        String totalDivCode,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static GoodItemDTO from(GoodEntity g) {
        return new GoodItemDTO(
                g.getId(),
                g.getGoodId(),
                g.getGoodName(),
                g.getTotalCnt(),
                g.getTotalDivCode(),
                g.getCreatedAt(),
                g.getUpdatedAt()
        );
    }
}