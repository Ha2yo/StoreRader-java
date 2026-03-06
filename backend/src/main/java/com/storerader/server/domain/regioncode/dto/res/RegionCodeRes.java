package com.storerader.server.domain.regioncode.dto.res;

import com.storerader.server.common.entity.RegionCodeEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지역 코드 응답")
public record RegionCodeRes(
        @Schema(description = "지역 코드", example = "020100000")
        String code,
        @Schema(description = "지역 이름", example = "서울특별시")
        String name,
        @Schema(description = "부모 코드", example = "020000000")
        String parentCode,
        @Schema(description = "지역 레벨", example = "1")
        Integer level
) {

    public static RegionCodeRes from(RegionCodeEntity r) {
        return new RegionCodeRes(
                r.getCode(),
                r.getName(),
                r.getParentCode(),
                r.getLevel()
        );
    }
}