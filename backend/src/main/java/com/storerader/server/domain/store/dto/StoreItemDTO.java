package com.storerader.server.domain.store.dto;

import com.storerader.server.common.entity.StoreEntity;

public record StoreItemDTO (
        Long id,
        Long storeId,
        String storeName,
        String telNo,
        String postNo,
        String jibunAddr,
        String roadAddr,
        Double lat,
        Double lng,
        String areaCode,
        String areaDetailCode
) {
    public static StoreItemDTO from(StoreEntity e) {
        return new StoreItemDTO(
                e.getId(),
                e.getStoreId(),
                e.getStoreName(),
                e.getTelNo(),
                e.getPostNo(),
                e.getJibunAddr(),
                e.getRoadAddr(),
                e.getLat(),
                e.getLng(),
                e.getAreaCode(),
                e.getAreaDetailCode()
        );
    }
}