package com.storerader.server.domain.admin.dto.select.stores;

import com.storerader.server.common.entity.StoreEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public record FindAllStoresListDTO(
        List<FindAllStoresDTO> stores,
        Long totalCount,
        int page,
        int size
) {
    public static FindAllStoresListDTO from(Page<StoreEntity> pageResult) {
        List<FindAllStoresDTO> stores = pageResult
                .getContent()
                .stream()
                .map(FindAllStoresDTO::from)
                .toList();

        return new FindAllStoresListDTO(
                stores,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }
}