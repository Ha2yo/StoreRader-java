package com.storerader.server.domain.regioncode.controller;

import com.storerader.server.domain.regioncode.dto.res.RegionCodeRes;
import com.storerader.server.domain.regioncode.service.RegionCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "지역 코드 API", description = "지역 코드 관련 API")
@RestController
@RequiredArgsConstructor
public class RegionCodeController {

    private final RegionCodeService regionCodeService;

    @GetMapping("/find/region-code/all")
    public List<RegionCodeRes> findAllStores() {
        return regionCodeService.findAllRegionCodes();
    }
}