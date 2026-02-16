package com.storerader.server.domain.admin.dto.select.users;
import com.storerader.server.common.entity.UserEntity;

import java.time.OffsetDateTime;

public record FindAllUsersDTO(
        Long id,
        String role,
        String name,
        String email,
        String sub,
        String picture,
        String refreshToken,
        OffsetDateTime refreshTokenExpiresAt,
        OffsetDateTime createdAt,
        OffsetDateTime lastLogin
) {
    public static FindAllUsersDTO from(UserEntity userEntity) {
        return new FindAllUsersDTO(
                userEntity.getId(),
                userEntity.getRole(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getSub(),
                userEntity.getPicture(),
                userEntity.getRefreshToken(),
                userEntity.getRefreshTokenExpiresAt(),
                userEntity.getCreatedAt(),
                userEntity.getLastLogin()
        );
    }
}