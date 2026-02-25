package com.storerader.server.common.repository.sql;

import com.storerader.server.common.entity.UserPreferenceEntity;
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

    public int increaseSelectionCount(Long userId) {
        Integer count = jdbcTemplate.queryForObject("""
                        UPDATE user_preferences
                        SET selection_count = selection_count + 1
                        WHERE id = ?
                        RETURNING selection_count
                        """,
                Integer.class,
                userId
        );

        if (count == null) {
            throw new IllegalStateException("selection_count 증가 실패");
        }

        return count;
    }

    public void updateUserWeights(
            long userId,
            double priceWeight,
            double distanceWeight) {
        jdbcTemplate.update("""
                        UPDATE user_preferences
                        SET price_weight = ?, distance_weight = ?
                        WHERE id = ?
                """,
                priceWeight,
                distanceWeight,
                userId
                );
    }
}