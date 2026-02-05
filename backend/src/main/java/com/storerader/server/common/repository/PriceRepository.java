package com.storerader.server.common.repository;

import com.storerader.server.common.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {
    boolean existsByInspectDay(String inspectDay);
}
