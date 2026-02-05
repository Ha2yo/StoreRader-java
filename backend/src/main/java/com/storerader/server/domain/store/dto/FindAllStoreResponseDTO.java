package com.storerader.server.domain.store.dto;

import java.util.List;

public record FindAllStoreResponseDTO(
        List<StoreItemDTO> stores
) {
    public static FindAllStoreResponseDTO of(List<StoreItemDTO> stores) {
        return new FindAllStoreResponseDTO(stores);
    }
}