package com.storerader.server.domain.admin.dto.select.stores;

import com.storerader.server.common.entity.StoreEntity;

import java.time.OffsetDateTime;

public record FindAllStoresDTO(
        Long id,
        Long store_id,
        String store_name,
        String tel_no,
        String post_no,
        String jibun_addr,
        String road_addr,
        Double lat,
        Double lng,
        String area_code,
        String area_detail_code,
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