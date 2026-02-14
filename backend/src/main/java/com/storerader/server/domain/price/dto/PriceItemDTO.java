package com.storerader.server.domain.price.dto;

public record PriceItemDTO(
        Long storeId,
        Integer price,
        String inspectDay
) {
}