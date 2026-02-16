package com.storerader.server.domain.admin.dto.select.prices;

import com.storerader.server.common.entity.PriceEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public record FindAllPricesListDTO(
        List<FindAllPricesDTO> prices,
        Long totalCount,
        int page,
        int sizs
) {
    public static FindAllPricesListDTO from(Page<PriceEntity> pageResult) {
        List<FindAllPricesDTO> prices = pageResult
                .getContent()
                .stream()
                .map(FindAllPricesDTO::from)
                .toList();

        return new FindAllPricesListDTO(
                prices,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }
}