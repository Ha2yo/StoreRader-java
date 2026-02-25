package com.storerader.server.common.repository;

import com.storerader.server.common.entity.UserSelectionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSelectionLogRepository extends JpaRepository<UserSelectionLogEntity, Long> {
    List<UserSelectionLogEntity> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
