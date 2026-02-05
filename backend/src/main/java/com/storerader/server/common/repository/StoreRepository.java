package com.storerader.server.common.repository;

import com.storerader.server.common.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    @Query("select s.storeId from StoreEntity s")
    List<Long> findAllStoreIds();

    @Query("select s.storeName from StoreEntity s where s.storeId = :storeId")
    String findStoreNameByStoreId(Long storeId);

    boolean existsByGoodId(Long goodId);
}
