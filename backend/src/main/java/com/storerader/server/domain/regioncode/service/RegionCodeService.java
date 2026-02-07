package com.storerader.server.domain.regioncode.service;

import com.storerader.server.common.repository.RegionCodeRepository;
import com.storerader.server.domain.regioncode.dto.FindAllRegionCodeResponseDTO;
import com.storerader.server.domain.regioncode.dto.RegionCodeItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionCodeService {
    private final RegionCodeRepository regionCodeRepository;

    public FindAllRegionCodeResponseDTO findAllRegionCodes() {
        var regionCodes = regionCodeRepository.findAll()
                .stream()
                .map(RegionCodeItemDTO::from)
                .toList();

        return FindAllRegionCodeResponseDTO.of(regionCodes);
    }
}
