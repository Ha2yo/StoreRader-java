package com.storerader.server.domain.admin.dto.select.prices;

import java.util.List;

public record FindAllPricesListDTO(
        List<FindAllPricesDTO> prices
) {
    public static FindAllPricesListDTO from(List<FindAllPricesDTO> prices) {
        return new FindAllPricesListDTO(prices);
    }
}