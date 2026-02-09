package com.storerader.server.domain.good.dto;

import java.util.List;

public record FindAllGoodResponseDTO(
        List<GoodItemDTO> goods
) {
    public static FindAllGoodResponseDTO of(List<GoodItemDTO> goods) {
        return new FindAllGoodResponseDTO(goods);
    }
}