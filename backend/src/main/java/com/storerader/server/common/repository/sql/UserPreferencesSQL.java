package com.storerader.server.common.repository.sql;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPreferencesSQL {
    private final JdbcTemplate jdbcTemplate;

    public void createDefaultPreference(Long userId) {
        jdbcTemplate.update("""
                        INSERT INTO user_preferences (
                            id,
                            distance_weight,
                            price_weight
                        )
                        VALUES (
                            ?,
                            0.5,
                            0.5
                        )
                        ON CONFLICT (id)
                        DO NOTHING
                        """,
                userId);
    }
}

