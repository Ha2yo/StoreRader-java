package com.storerader.server.common.repository;

import com.storerader.server.common.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    Optional<StoreEntity> findByStoreId(Integer storeId);
    boolean existsByStoreId(Integer storeId);
}
