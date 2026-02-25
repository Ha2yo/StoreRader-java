package com.storerader.server.common.repository.sql;

import com.storerader.server.domain.userSelectionLog.dto.UserSelectionLogReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSelectionLogSQL {
    private final JdbcTemplate jdbcTemplate;

    public void insertUserSelectionLog(
            Long userId,
            UserSelectionLogReqDTO req
    ) {
        jdbcTemplate.update("""
                        INSERT INTO user_selection_log (
                            user_id,
                            store_id,
                            good_id,
                            preference_type,
                            price
                            )
                            VALUES (?, ?, ?, ?, ?)
                        """,
                userId,
                req.storeId(),
                req.goodId(),
                req.preferenceType(),
                req.price()
        );
    }
}
