package com.storerader.server.domain.admin.dto.select.regionCodes;

import com.storerader.server.common.entity.RegionCodeEntity;

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
