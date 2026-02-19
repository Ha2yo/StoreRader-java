package com.storerader.server.common.repository;

import com.storerader.server.common.entity.UserSelectionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSelectionLogRepository extends JpaRepository<UserSelectionLogEntity, Long> {
}
