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
@Table(name = "stores")
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "tel_no")
    private String telNo;

    @Column(name = "post_no")
    private String postNo;

    @Column(name = "jibun_addr")
    private String jibunAddr;

    @Column(name = "road_addr")
    private String roadAddr;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "area_code")
    private String areaCode;

    @Column(name = "area_detail_code")
    private String areaDetailCode;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public StoreEntity(
            Long storeId,
            String storeName,
            String telNo,
            String postNo,
            String jibunAddr,
            String roadAddr,
            Double lat,
            Double lng,
            String areaCode,
            String areaDetailCode
    ) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.telNo = telNo;
        this.postNo = postNo;
        this.jibunAddr = jibunAddr;
        this.roadAddr = roadAddr;
        this.lat = lat;
        this.lng = lng;
        this.areaCode = areaCode;
        this.areaDetailCode = areaDetailCode;
    }
}
