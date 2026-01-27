package com.storerader.server.admin.dto;
import com.storerader.server.common.entity.UserEntity;

import java.time.OffsetDateTime;

public record FindAllUsersResponse(
        Long id,
        String name,
        String email,
        String role,
        OffsetDateTime createdAt,
        OffsetDateTime lastLogin
) {
    public static FindAllUsersResponse from(UserEntity entity) {
        return new FindAllUsersResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getLastLogin()
        );
    }
}