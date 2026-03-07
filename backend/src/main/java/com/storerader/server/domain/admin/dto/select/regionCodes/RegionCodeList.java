package com.storerader.server.domain.admin.dto.select.regionCodes;

import com.storerader.server.common.entity.RegionCodeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "지역코드 목록 조회 응답")
public record RegionCodeList(
        @Schema(description = "지역코드 목록")
        List<RegionCode> regionCodes,

        @Schema(description = "전체 상품 개수")
        long totalCount,

        @Schema(description = "현재 페이지 번호")
        int page,

        @Schema(description = "페이지 크기")
        int size
) {
    public static RegionCodeList from(Page<RegionCodeEntity> pageResult) {
        List<RegionCode> regionCodes = pageResult
                .getContent()
                .stream()
                .map(RegionCode::from)
                .toList();

        return new RegionCodeList(
                regionCodes,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }
}
