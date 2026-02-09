package com.storerader.server.domain.good.controller;

import com.storerader.server.domain.good.dto.FindAllGoodResponseDTO;
import com.storerader.server.domain.good.service.GoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GoodController {
    private final GoodService goodService;

    @GetMapping("/find/good/all")
    public FindAllGoodResponseDTO findAllGoods() {
        return goodService.findAllGoods();
    }
}
