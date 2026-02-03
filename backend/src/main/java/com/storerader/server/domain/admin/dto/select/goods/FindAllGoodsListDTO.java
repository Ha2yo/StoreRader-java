package com.storerader.server.domain.admin.dto.select.goods;

import java.util.List;

public record FindAllGoodsListDTO(
        List<FindAllGoodsDTO> goods
) {
    public static FindAllGoodsListDTO from(List<FindAllGoodsDTO> goods) {
        return new FindAllGoodsListDTO(goods);
    }
}