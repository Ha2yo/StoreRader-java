package com.storerader.server.domain.admin.dto.select.regionCodes;

import com.storerader.server.common.entity.RegionCodeEntity;
import com.storerader.server.common.entity.UserEntity;
import com.storerader.server.domain.admin.dto.select.users.FindAllUsersDTO;

import java.time.OffsetDateTime;

public record FindAllRegionCodesDTO (
    String code,
    String name,
    String parentCode,
    Integer level
) {
    public static FindAllRegionCodesDTO from(RegionCodeEntity regionCodeEntity) {
        return new FindAllRegionCodesDTO(
                regionCodeEntity.getCode(),
                regionCodeEntity.getName(),
                regionCodeEntity.getParentCode(),
                regionCodeEntity.getLevel()
        );
    }
}
