package com.storerader.server.common.repository;

import com.storerader.server.common.entity.RegionCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<RegionCodeEntity, Long> {
}
