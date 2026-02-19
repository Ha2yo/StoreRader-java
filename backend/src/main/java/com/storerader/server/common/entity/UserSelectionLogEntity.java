package com.storerader.server.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_selection_log")
public class UserSelectionLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "good_id", nullable = false)
    private Integer goodId;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "preference_type")
    private String preferenceType;

    @Column(name = "created_at", nullable = false)
    private String createdAt;
}
