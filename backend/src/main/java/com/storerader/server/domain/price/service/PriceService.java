package com.storerader.server.domain.price.service;

import com.storerader.server.common.repository.sql.PriceRepositorySQL;
import com.storerader.server.domain.price.dto.res.PriceItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {

    public final PriceRepositorySQL priceRepositorySQL;

    /**
     * 특정 상품에 대한 매장별 가격 정보를 반환한다.
     *
     * @param goodName 상품 이름
     * @return 해당 상품에 대한 매장별 가격 리스트
     */
    public List<PriceItemDTO> findPrices(
            String goodName
    ) {
        return priceRepositorySQL.findLatestPricesByGoodName(goodName);
    }
}