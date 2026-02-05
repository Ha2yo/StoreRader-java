package com.storerader.server.common.repository.sql;

import com.storerader.server.common.entity.PriceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PriceRepositorySQL {
    private final JdbcTemplate jdbcTemplate;

    public int upsertPrices(PriceEntity price) {
        return jdbcTemplate.update(
                """
                INSERT INTO prices (
                            good_id,
                            store_id,
                            inspect_day,
                            price,
                            is_one_plus_one,
                            is_discount,
                            discount_start,
                            discount_end
                        )
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        ON CONFLICT (good_id, store_id, inspect_day)
                        DO UPDATE SET
                            price = EXCLUDED.price,
                            is_one_plus_one = EXCLUDED.is_one_plus_one,
                            is_discount = EXCLUDED.is_discount,
                            discount_start = EXCLUDED.discount_start,
                            discount_end = EXCLUDED.discount_end
                """,
                price.getGoodId(),
                price.getStoreId(),
                price.getInspectDay(),
                price.getPrice(),
                price.getIsOnePlusOne(),
                price.getIsDiscount(),
                price.getDiscountStart(),
                price.getDiscountEnd()
        );
    }
}