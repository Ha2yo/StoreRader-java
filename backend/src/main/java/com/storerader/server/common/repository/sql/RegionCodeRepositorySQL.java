package com.storerader.server.common.repository.sql;

import com.storerader.server.common.entity.RegionCodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RegionCodeRepositorySQL {
    private final JdbcTemplate jdbcTemplate;

    public int[][] upsertRegionCodes(List<RegionCodeEntity> regionCodes) {
        String sql = """
                INSERT INTO regions (
                            code,
                            name,
                            parent_code,
                            level
                        )
                        VALUES (?, ?, ?, ?)
                        ON CONFLICT (code)
                        DO UPDATE SET
                            code = EXCLUDED.code,
                            name = EXCLUDED.name,
                            parent_code = EXCLUDED.parent_code,
                            level = EXCLUDED.level
                        WHERE
                            regions.code IS DISTINCT FROM EXCLUDED.code
                            OR regions.name IS DISTINCT FROM EXCLUDED.name
                """;

        return jdbcTemplate.batchUpdate(sql, regionCodes, 1000, (ps, regionCode) -> {
            ps.setString(1, regionCode.getCode());
            ps.setString(2, regionCode.getName());
            ps.setString(3, regionCode.getParentCode());
            ps.setInt(4, regionCode.getLevel());
        });
    }
}
