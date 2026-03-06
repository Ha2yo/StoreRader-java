package com.storerader.server.domain.regioncode.controller;

import com.storerader.server.domain.regioncode.dto.res.RegionCodeRes;
import com.storerader.server.domain.regioncode.service.RegionCodeService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "전체 지역 코드 목록 조회",
            description = "DB에 등록된 모든 지역 코드를 리스트로 반환합니다."
    )
    @GetMapping("/find/region-code/all")
    public List<RegionCodeRes> findAllStores() {
        return regionCodeService.findAllRegionCodes();
    }
}