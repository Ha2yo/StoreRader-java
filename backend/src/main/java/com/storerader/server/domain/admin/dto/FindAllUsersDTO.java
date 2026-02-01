package com.storerader.server.domain.admin.dto;
import com.storerader.server.common.entity.UserEntity;

import java.time.OffsetDateTime;

public record FindAllUsers(
        Long id,
        String name,
        String email,
        String role,
        OffsetDateTime createdAt,
        OffsetDateTime lastLogin
) {
    public static FindAllUsers from(UserEntity userEntity) {
        return new FindAllUsers(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getRole(),
                userEntity.getCreatedAt(),
                userEntity.getLastLogin()
        );
    }
}