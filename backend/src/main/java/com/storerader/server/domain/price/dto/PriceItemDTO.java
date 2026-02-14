package com.storerader.server.domain.price.dto;

import com.storerader.server.common.entity.PriceEntity;

import java.time.OffsetDateTime;

public record PriceItemDTO(
        Integer storeId,
        Integer price,
        String inspectDay
) {
}