package com.storerader.server.common.repository.sql;

import com.storerader.server.common.entity.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreRepositorySQL {
    private final JdbcTemplate jdbcTemplate;

    public int[][] upsertStores(List<StoreEntity> stores) {
        String sql = """
                INSERT INTO stores (
                            store_id,
                            store_name,
                            tel_no,
                            post_no,
                            jibun_addr,
                            road_addr,
                            lat,
                            lng,
                            area_code,
                            area_detail_code
                        )
                        VALUES (?, ?, ?, ?, ?,
                                ?, ?, ?, ?, ?)
                        ON CONFLICT (store_id)
                        DO UPDATE SET
                            store_id = EXCLUDED.store_id,
                            store_name = EXCLUDED.store_name,
                            tel_no = EXCLUDED.tel_no,
                            post_no = EXCLUDED.post_no,
                            jibun_addr = EXCLUDED.jibun_addr,
                            road_addr = EXCLUDED.road_addr,
                            lat = EXCLUDED.lat,
                            lng = EXCLUDED.lng,
                            area_code = EXCLUDED.area_code,
                            area_detail_code = EXCLUDED.area_detail_code,
                            updated_at = NOW()
                        WHERE
                            stores.store_name IS DISTINCT FROM EXCLUDED.store_name
                            OR stores.tel_no IS DISTINCT FROM EXCLUDED.tel_no
                            OR stores.post_no IS DISTINCT FROM EXCLUDED.post_no
                            OR stores.jibun_addr IS DISTINCT FROM EXCLUDED.jibun_addr
                            OR stores.road_addr IS DISTINCT FROM EXCLUDED.road_addr
                """;

        return jdbcTemplate.batchUpdate(sql, stores, 1000, (ps, store) -> {
            ps.setLong(1, store.getStoreId());
            ps.setString(2, store.getStoreName());
            ps.setString(3, store.getTelNo());
            ps.setString(4, store.getPostNo());
            ps.setString(5, store.getJibunAddr());
            ps.setString(6, store.getRoadAddr());
            ps.setDouble(7, store.getLat());
            ps.setDouble(8, store.getLng());
            ps.setString(9, store.getAreaCode());
            ps.setString(10, store.getAreaDetailCode());
        });
    }
}
