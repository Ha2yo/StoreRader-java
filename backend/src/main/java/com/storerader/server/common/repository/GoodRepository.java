package com.storerader.server.common.repository;

import com.storerader.server.common.entity.GoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodRepository extends JpaRepository<GoodEntity, Long> {
    Optional<GoodEntity> findByGoodId(Integer goodId);
    boolean existsByGoodId(Integer goodId);
}
