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
@Table(name = "prices")
public class PriceEntity {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "good_id", nullable = false)
    private Integer goodId;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "inspect_day", nullable = false)
    private String inspectDay;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "is_one_plus_one")
    private String isOnePlusOne;

    @Column(name = "is_discount")
    private String isDiscount;

    @Column(name = "discount_start")
    private String discountStart;

    @Column(name = "discount_end")
    private String discountEnd;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public PriceEntity(
            Integer id,
            Integer goodId,
            Integer storeId,
            String inspectDay,
            Integer price,
            String isOnePlusOne,
            String isDiscount,
            String discountStart,
            String discountEnd
    ) {
        this.id = id;
        this.goodId = goodId;
        this.storeId = storeId;
        this.inspectDay = inspectDay;
        this.price = price;
        this.isOnePlusOne = isOnePlusOne;
        this.isDiscount = isDiscount;
        this.discountStart = discountStart;
        this.discountEnd = discountEnd;
    }
}

