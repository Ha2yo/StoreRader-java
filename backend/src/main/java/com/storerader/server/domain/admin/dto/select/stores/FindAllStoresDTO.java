package com.storerader.server.domain.admin.dto.select.stores;

import com.storerader.server.common.entity.StoreEntity;

import java.time.OffsetDateTime;

public record FindAllStoresDTO(
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
        String areaDetailCode,
        OffsetDateTime createdAt,
        OffsetDateTime lastLogin
) {
    public static FindAllStoresDTO from(StoreEntity storeEntity) {
        return new FindAllStoresDTO(
                storeEntity.getId(),
                storeEntity.getStoreId(),
                storeEntity.getStoreName(),
                storeEntity.getTelNo(),
                storeEntity.getPostNo(),
                storeEntity.getJibunAddr(),
                storeEntity.getRoadAddr(),
                storeEntity.getLat(),
                storeEntity.getLng(),
                storeEntity.getAreaCode(),
                storeEntity.getAreaDetailCode(),
                storeEntity.getCreatedAt(),
                storeEntity.getUpdatedAt()
        );
    }
}