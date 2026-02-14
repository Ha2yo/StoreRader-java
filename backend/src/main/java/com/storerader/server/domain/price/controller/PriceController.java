package com.storerader.server.domain.price.controller;

import com.storerader.server.domain.price.dto.FindPriceResponseDTO;
import com.storerader.server.domain.price.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @GetMapping("/find/price")
    public FindPriceResponseDTO findPrices(
            @RequestParam String goodName
    ) {
        return priceService.findPrices(goodName);
    }
}