package com.storerader.server.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sub", unique = true, nullable = false)
    private String sub;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_login")
    private OffsetDateTime lastLogin;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "refresh_token_expires_at")
    private OffsetDateTime refreshTokenExpiresAt;

    @Column(name = "role")
    private String role;

    @Column(name = "picture")
    private String picture;

    @Builder
    public UserEntity(
            Long id,
            String sub,
            String email,
            String name,
            OffsetDateTime createdAt,
            OffsetDateTime lastLogin,
            String refreshToken,
            OffsetDateTime refreshTokenExpiresAt,
            String role,
            String picture
    ) {
        this.sub = sub;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
        this.role = role;
        this.picture = picture;
    }
}