package com.storerader.server.domain.regioncode.dto;

import java.util.List;

public record FindAllRegionCodeResponseDTO(
        List<RegionCodeItemDTO> regionCodes
) {
    public static FindAllRegionCodeResponseDTO of(List<RegionCodeItemDTO> regionCodes) {
        return new FindAllRegionCodeResponseDTO(regionCodes);
    }
}