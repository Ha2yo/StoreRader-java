package com.storerader.server.domain.regioncode.controller;

import com.storerader.server.domain.regioncode.dto.FindAllRegionCodeResponseDTO;
import com.storerader.server.domain.regioncode.service.RegionCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegionCodeController {
    private final RegionCodeService regionCodeService;

    @GetMapping("/find/region-code/all")
    public FindAllRegionCodeResponseDTO findAllStores() {
        return regionCodeService.findAllRegionCodes();
    }
}
