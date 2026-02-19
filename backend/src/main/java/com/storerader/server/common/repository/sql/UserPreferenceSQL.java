package com.storerader.server.common.repository.sql;

import com.storerader.server.domain.userPreference.dto.UserPreferenceItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPreferenceSQL {
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

    public UserPreferenceItemDTO findPreferenceByUserId(Long userId) {
        return jdbcTemplate.queryForObject("""
                        SELECT *
                            FROM user_preferences
                            WHERE id = ?
                        """,
                (rs, rowNum) -> new UserPreferenceItemDTO(
                        rs.getDouble("distance_weight"),
                        rs.getDouble("price_weight")
                ),
                userId);
    }
}