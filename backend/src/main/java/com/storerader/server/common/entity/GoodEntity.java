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
@Table(name = "goods")
public class GoodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "good_id", nullable = false)
    private Integer goodId;

    @Column(name = "good_name", nullable = false)
    private String goodName;

    @Column(name = "total_cnt")
    private Integer totalCnt;

    @Column(name = "total_div_code")
    private String totalDivCode;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public GoodEntity(
            Integer goodId,
            String goodName,
            Integer totalCnt,
            String totalDivCode
    ) {
        this.goodId = goodId;
        this.goodName = goodName;
        this.totalCnt = totalCnt;
        this.totalDivCode = totalDivCode;
    }
}
