package com.storerader.server.common.repository.sql;

import com.storerader.server.common.entity.GoodEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GoodRepositorySQL {
    private final JdbcTemplate jdbcTemplate;

    public int[][] upsertGoods(List<GoodEntity> goods) {
        String sql = """
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
                    WHERE
                        goods.good_name IS DISTINCT FROM EXCLUDED.good_name
                        OR goods.total_cnt IS DISTINCT FROM EXCLUDED.total_cnt
                        OR goods.total_div_code IS DISTINCT FROM EXCLUDED.total_div_code
            """;

        return jdbcTemplate.batchUpdate(sql, goods, 1000, (ps, good) -> {
            ps.setInt(1, good.getGoodId());
            ps.setString(2, good.getGoodName());
            ps.setObject(3, good.getTotalCnt()); // Integer 타입이므로 null 허용을 위해 setObject 사용
            ps.setString(4, good.getTotalDivCode());
        });
    }
}
