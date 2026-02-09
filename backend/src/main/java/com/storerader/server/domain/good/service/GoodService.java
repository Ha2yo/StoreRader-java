package com.storerader.server.domain.good.service;

import com.storerader.server.common.repository.GoodRepository;
import com.storerader.server.domain.good.dto.FindAllGoodResponseDTO;
import com.storerader.server.domain.good.dto.GoodItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodService {
    private final GoodRepository goodRepository;

    public FindAllGoodResponseDTO findAllGoods() {
        var goods = goodRepository.findAll()
                .stream()
                .map(GoodItemDTO::from)
                .toList();

        return FindAllGoodResponseDTO.of(goods);
    }
}
