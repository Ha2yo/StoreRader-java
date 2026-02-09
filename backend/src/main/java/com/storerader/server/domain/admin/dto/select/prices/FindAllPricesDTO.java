package com.storerader.server.domain.admin.dto.select.prices;
import com.storerader.server.common.entity.PriceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;

public record FindAllPricesDTO(
        int id,
        int goodId,
        long storeId,
        String inspectDay,
        Integer price,
        String isOnePlusOne,
        String isDiscount,
        String discountStart,
        String discountEnd,
        OffsetDateTime createdAt
) {
    public static FindAllPricesDTO from(PriceEntity priceEntity) {
        return new FindAllPricesDTO(
                priceEntity.getId(),
                priceEntity.getGoodId(),
                priceEntity.getStoreId(),
                priceEntity.getInspectDay(),
                priceEntity.getPrice(),
                priceEntity.getIsOnePlusOne(),
                priceEntity.getIsDiscount(),
                priceEntity.getDiscountStart(),
                priceEntity.getDiscountEnd(),
                priceEntity.getCreatedAt()
        );
    }
}
