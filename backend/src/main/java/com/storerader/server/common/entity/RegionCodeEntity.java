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
public class RegionCodeEntity {
    @Id
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "level")
    private Integer level;

    public RegionCodeEntity(
            String code,
            String name,
            String parentCode,
            Integer level
    ) {
        this.code = code;
        this.name = name;
        this.parentCode = parentCode;
        this.level = level;
    }
}
