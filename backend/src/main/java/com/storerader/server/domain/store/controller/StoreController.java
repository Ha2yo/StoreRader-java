package com.storerader.server.domain.store.controller;

import com.storerader.server.domain.store.dto.res.StoreRes;
import com.storerader.server.domain.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "매장 API", description = "매장 정보 관련 API")
@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @Operation(
            summary = "전체 매장 목록 조회",
            description = "DB에 등록된 모든 매장 정보를 리스트로 반환합니다."
    )
    @GetMapping("/stores")
    public List<StoreRes> findAllStores() {
        return storeService.findAllStores();
    }
}