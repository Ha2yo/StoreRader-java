package com.storerader.server.common.repository;

import com.storerader.server.common.entity.GoodEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GoodRepositorySQL {
    private final JdbcTemplate jdbcTemplate;

    public int upsertGoods(GoodEntity good) {
        return jdbcTemplate.update(
            """
            INSERT INTO goods (
                        good_id,
                        good_name,
                        total_cnt,
                        total_div_code
                    )
                    VALUES (?, ?, ?, ?)
                    ON CONFLICT (good_id)
                    DO UPDATE SET
                        good_name = EXCLUDED.good_name,
                        total_cnt = EXCLUDED.total_cnt,
                        total_div_code = EXCLUDED.total_div_code,
                        updated_at = NOW()
            """,
                good.getGoodId(),
                good.getGoodName(),
                good.getTotalCnt(),
                good.getTotalDivCode()
        );
    }
}
