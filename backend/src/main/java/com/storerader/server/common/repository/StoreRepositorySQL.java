package com.storerader.server.common.repository;

import com.storerader.server.common.entity.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreRepositorySQL {
    private final JdbcTemplate jdbcTemplate;

    public int upsertStores(StoreEntity store) {
        return jdbcTemplate.update(
                """
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
                            store_name = EXCLUDED.store_name,
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
                """,
                store.getStoreId(),
                store.getStoreName(),
                store.getTelNo(),
                store.getPostNo(),
                store.getJibunAddr(),
                store.getRoadAddr(),
                store.getLat(),
                store.getLng(),
                store.getAreaCode(),
                store.getAreaDetailCode()
        );
    }
}
