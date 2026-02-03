package com.storerader.server.domain.admin.dto;

import java.util.List;

public record FindAllGoodsListResponseDTO(
        List<FindAllGoodsDTO> goods
) {
    public static FindAllGoodsListResponseDTO from(List<FindAllGoodsDTO> goods) {
        return new FindAllGoodsListResponseDTO(goods);
    }
}