package com.storerader.server.domain.admin.dto.select.regionCodes;

import com.storerader.server.common.entity.RegionCodeEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public record FindAllRegionCodesListDTO (
        List<FindAllRegionCodesDTO> regionCodes,
        Long totalCount,
        int page,
        int size
) {
    public static FindAllRegionCodesListDTO from(Page<RegionCodeEntity> pageResult) {
        List<FindAllRegionCodesDTO> regionCodes = pageResult
                .getContent()
                .stream()
                .map(FindAllRegionCodesDTO::from)
                .toList();

        return new FindAllRegionCodesListDTO(
                regionCodes,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }
}





