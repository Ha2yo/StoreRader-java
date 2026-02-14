package com.storerader.server.domain.price.service;

import com.storerader.server.common.repository.sql.PriceRepositorySQL;
import com.storerader.server.domain.price.dto.FindPriceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {
    public final PriceRepositorySQL priceRepositorySQL;

    public FindPriceResponseDTO findPrices(
            String goodName
    ) {
        var prices = priceRepositorySQL.findLatestPricesByGoodName(goodName);

        return FindPriceResponseDTO.of(prices);
    }
}
