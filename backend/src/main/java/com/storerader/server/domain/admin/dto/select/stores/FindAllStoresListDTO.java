package com.storerader.server.domain.admin.dto.select.stores;

import java.util.List;

public record FindAllStoresListDTO(
        List<FindAllStoresDTO> stores
) {
    public static FindAllStoresListDTO from(List<FindAllStoresDTO> stores) {
        return new FindAllStoresListDTO(stores);
    }
}