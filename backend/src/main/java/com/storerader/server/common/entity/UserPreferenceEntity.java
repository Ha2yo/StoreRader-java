package com.storerader.server.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_preferences")
public class UserPreferenceEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "selection_count")
    private Integer selectionCount;

    @Column(name = "distance_weight")
    private Double distanceWeight;

    @Column(name = "price_weight")
    private Double priceWeight;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
