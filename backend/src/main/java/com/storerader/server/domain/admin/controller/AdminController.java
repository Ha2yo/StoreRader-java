package com.storerader.server.domain.admin.controller;

import com.storerader.server.domain.admin.dto.select.goods.GoodList;
import com.storerader.server.domain.admin.dto.select.prices.PriceList;
import com.storerader.server.domain.admin.dto.select.regionCodes.RegionCodeList;
import com.storerader.server.domain.admin.dto.select.stores.StoreList;
import com.storerader.server.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "관리자 API", description = "관리자 전용 데이터 관리 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(
            summary = "전체 상품 조회",
            description = "상품 테이블의 데이터를 페이지 단위로 조회합니다."
    )
    @GetMapping("/select/goods")
    public ResponseEntity<GoodList> getAllGoods(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam int size,

            @Parameter(description = "정렬 기준 컬럼", example = "id")
            @RequestParam String sortKey,

            @Parameter(description = "정렬 방향 (asc / desc)", example = "asc")
            @RequestParam String sortOrder
    ) {
        return ResponseEntity.ok(adminService.findAllGoods(page, size, sortKey, sortOrder));
    }

    @Operation(
            summary = "전체 매장 조회",
            description = "매장 테이블의 데이터를 페이지 단위로 조회합니다."
    )
    @GetMapping("/select/stores")
    public ResponseEntity<StoreList> getAllStores(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam int size,

            @Parameter(description = "정렬 기준 컬럼", example = "id")
            @RequestParam String sortKey,

            @Parameter(description = "정렬 방향 (asc / desc)", example = "asc")
            @RequestParam String sortOrder
    ) {
        return ResponseEntity.ok(adminService.findAllStores(page, size, sortKey, sortOrder));
    }

    @Operation(
            summary = "전체 지역코드 조회",
            description = "지역코드 테이블의 데이터를 페이지 단위로 조회합니다."
    )
    @GetMapping("/select/region-codes")
    public ResponseEntity<RegionCodeList> getAllRegionCodes(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam int size,

            @Parameter(description = "정렬 기준 컬럼", example = "code")
            @RequestParam String sortKey,

            @Parameter(description = "정렬 방향 (asc / desc)", example = "asc")
            @RequestParam String sortOrder
    ) {
        return ResponseEntity.ok(adminService.findAllRegionCodes(page, size, sortKey, sortOrder));
    }

    @Operation(
            summary = "전체 가격 조회",
            description = "가격 테이블의 데이터를 페이지 단위로 조회합니다."
    )
    @GetMapping("/select/prices")
    public ResponseEntity<PriceList> getAllPriceCodes(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam int size,

            @Parameter(description = "정렬 기준 컬럼", example = "ideeeee")
            @RequestParam String sortKey,

            @Parameter(description = "정렬 방향 (asc / desc)", example = "asc")
            @RequestParam String sortOrder
    ) {
        return ResponseEntity.ok(adminService.findAllPrices(page, size, sortKey, sortOrder));
    }

    @Operation(
            summary = "공공데이터 상품 수집",
            description = "공공데이터포털의 상품 데이터를 조회하여 DB에 적재합니다."
    )
    @GetMapping(value = "/get/public-data/goods", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter fetchGoodsApi() {
        return adminService.fetchGoodsApi();
    }

    @Operation(
            summary = "공공데이터 매장 수집",
            description = "공공데이터포털의 매장 데이터를 조회하여 DB에 적재합니다."
    )
    @GetMapping(value = "/get/public-data/stores", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter fetchStoresApi() {
        return adminService.fetchStoresApi();
    }

    @Operation(
            summary = "공공데이터 지역코드 수집",
            description = "공공데이터포털의 지역코드 데이터를 조회하여 DB에 적재합니다."
    )
    @GetMapping(value = "/get/public-data/regionCodes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter fetchRegionCodesApi() {
        return adminService.fetchRegionCodesApi();
    }

    @Operation(
            summary = "공공데이터 가격 수집",
            description = "공공데이터포털의 가격 데이터를 조회하여 DB에 적재합니다."
    )
    @GetMapping(value = "/get/public-data/prices", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter fetchPricesApi(
            @RequestParam String inspectDay
    ) {
        return adminService.fetchPricesApi(inspectDay);
    }
}