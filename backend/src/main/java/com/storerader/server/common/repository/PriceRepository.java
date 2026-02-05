package com.storerader.server.common.repository;

import com.storerader.server.common.entity.GoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<GoodEntity, Long> {
    boolean existsByInspectDay(String inspectDay);
}
