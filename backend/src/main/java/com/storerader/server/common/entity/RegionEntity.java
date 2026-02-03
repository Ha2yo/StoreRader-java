package com.storerader.server.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "regions")
public class RegionEntity {
    @Column(name = "code", nullable = false)
    private Long code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_code")
    private String parent_code;

    @Column(name = "level")
    private String level;

    public RegionEntity(
            Long code,
            String name,
            String parent_code,
            String level
    ) {
        this.code = code;
        this.name = name;
        this.parent_code = parent_code;
        this.level = level;
    }
}
