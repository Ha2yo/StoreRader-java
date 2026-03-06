package com.storerader.server.domain.good.controller;

import com.storerader.server.domain.good.dto.res.GoodRes;
import com.storerader.server.domain.good.service.GoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "상품 API", description = "상품 정보 관련 API")
@RestController
@RequiredArgsConstructor
public class GoodController {
    private final GoodService goodService;

    @Operation(
            summary = "전체 상품 목록 조회",
            description = "DB에 등록된 모든 상품 정보를 리스트로 반환합니다."
    )
    @GetMapping("/find/good/all")
    public List<GoodRes> findAllGoods() {
        return goodService.findAllGoods();
    }
}
