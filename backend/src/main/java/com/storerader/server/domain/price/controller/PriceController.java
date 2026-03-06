package com.storerader.server.domain.price.controller;

import com.storerader.server.domain.price.dto.res.PriceRes;
import com.storerader.server.domain.price.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "가격 API", description = "가격 관련 API")
@RestController
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @Operation(
            summary = "특정 상품에 대한 가격 목록 조회",
            description = "사용자가 입력한 상품에 대한 각 매장별 가격 리스트를 반환합니다."
    )
    @GetMapping("/find/price")
    public List<PriceRes> findPrices(
            @RequestParam(name = "good-name") String goodName
    ) {
        return priceService.findPrices(goodName);
    }
}