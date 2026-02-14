package com.storerader.server.domain.price.dto;

import java.util.List;

public record FindPriceResponseDTO(
        List<PriceItemDTO> prices
) {
    public static FindPriceResponseDTO of(List<PriceItemDTO> prices) {
        return new FindPriceResponseDTO(prices);
    }
}