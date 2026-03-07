package com.storerader.server.domain.admin.dto.select.regionCodes;

import com.storerader.server.common.entity.RegionCodeEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지역코드 조회 응답")
public record RegionCode(
        @Schema(description = "지역 코드", example = "020100000")
        String code,

        @Schema(description = "지역 이름", example = "서울특별시")
        String name,

        @Schema(description = "부모 코드", example = "020000000")
        String parentCode,

        @Schema(description = "코드 레벨", example = "1")
        Integer level
) {
    public static RegionCode from(RegionCodeEntity regionCodeEntity) {
        return new RegionCode(
                regionCodeEntity.getCode(),
                regionCodeEntity.getName(),
                regionCodeEntity.getParentCode(),
                regionCodeEntity.getLevel()
        );
    }
}
