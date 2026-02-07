package com.storerader.server.domain.regioncode.dto;

import com.storerader.server.common.entity.RegionCodeEntity;

public record RegionCodeItemDTO (
        String code,
        String name,
        String parentCode,
        Integer level
) {
    public static RegionCodeItemDTO from(RegionCodeEntity r) {
        return new RegionCodeItemDTO(
                r.getCode(),
                r.getName(),
                r.getParentCode(),
                r.getLevel()
        );
    }
}