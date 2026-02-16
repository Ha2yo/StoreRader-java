package com.storerader.server.common.repository;

import com.storerader.server.common.entity.GoodEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodRepository extends JpaRepository<GoodEntity, Long> {
    Page<GoodEntity> findByGoodNameContainingIgnoreCase(String q, Pageable pageable);
}
