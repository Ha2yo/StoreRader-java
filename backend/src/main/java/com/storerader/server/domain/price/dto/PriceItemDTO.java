package com.storerader.server.domain.price.dto;

public record PriceItemDTO(
        Integer storeId,
        Integer price,
        String inspectDay
) {
}