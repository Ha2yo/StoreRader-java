package com.storerader.server.common.repository.sql;

import com.storerader.server.common.entity.PriceEntity;
import com.storerader.server.domain.price.dto.PriceItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<PriceItemDTO> findLatestPricesByGoodName(String goodName) {
        return jdbcTemplate.query(
                """
                        SELECT
                            p.store_id,
                            p.price,
                            p.inspect_day
                        FROM prices p
                        JOIN (
                            SELECT
                                store_id,
                                MAX(inspect_day) AS latest_day
                            FROM prices
                            WHERE good_id = (
                                SELECT good_id
                                FROM goods
                                WHERE good_name = ?
                                LIMIT 1
                            )
                            GROUP BY store_id
                        ) latest
                            ON p.store_id = latest.store_id
                            AND p.inspect_day = latest.latest_day
                        WHERE p.good_id = (
                            SELECT good_id
                            FROM goods
                            WHERE good_name = ?
                            LIMIT 1
                        )
                        """,
                (rs, rowNum) -> new PriceItemDTO(
                        rs.getLong("store_id"),
                        rs.getInt("price"),
                        rs.getString("inspect_day")
                ),
                goodName,
                goodName
        );
    }
}