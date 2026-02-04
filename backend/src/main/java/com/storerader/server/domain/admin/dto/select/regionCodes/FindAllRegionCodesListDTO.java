package com.storerader.server.domain.admin.dto.select.regionCodes;

import java.util.List;

public record FindAllRegionCodesListDTO (
        List<FindAllRegionCodesDTO> regionCodes
) {
    public static FindAllRegionCodesListDTO from(List<FindAllRegionCodesDTO> regionCodes) {
        return new FindAllRegionCodesListDTO(regionCodes);
    }
}





