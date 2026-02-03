package com.storerader.server.common.repository.sql;

import com.storerader.server.common.entity.RegionCodeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RegionCodeRepositorySQL {
    private final JdbcTemplate jdbcTemplate;

    public int upsertRegionCodes(RegionCodeEntity regionCode) {
        return jdbcTemplate.update(
                """
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
                """,
                regionCode.getCode(),
                regionCode.getName(),
                regionCode.getParentCode(),
                regionCode.getLevel()
        );
    }
}
