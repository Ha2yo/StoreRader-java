package com.storerader.server.domain.admin.dto.select.stores;

import com.storerader.server.common.entity.StoreEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "매장 조회 응답")
public record Store(
        @Schema(description = "DB PK", example = "1")
        Long id,

        @Schema(description = "매장 ID", example = "786")
        Long storeId,

        @Schema(description = "매장 이름", example = "이마트 연수점")
        String storeName,

        @Schema(description = "전화번호", example = "032-820-1234")
        String telNo,

        @Schema(description = "우편번호", example = "21975")
        String postNo,

        @Schema(description = "지번 주소", example = "인천광역시 연수구 동춘동 926-9 이마트")
        String jibunAddr,

        @Schema(description = "도로명 주소", example = "인천광역시 연수구 경원대로 184")
        String roadAddr,

        @Schema(description = "위도 좌표", example = "37.404396292")
        Double lat,

        @Schema(description = "경도 좌표", example = "126.681271854")
        Double lng,

        @Schema(description = "지역 코드", example = "020700000")
        String areaCode,

        @Schema(description = "상세 지역 코드", example = "020710000")
        String areaDetailCode,

        @Schema(description = "레코드 생성일시", example = "2026-02-04 01:59:54.082358+09")
        OffsetDateTime createdAt,

        @Schema(description = "레코드 수정일시", example = "2026-02-04 03:12:38.761663+09")
        OffsetDateTime updatedAt
) {
    public static Store from(StoreEntity storeEntity) {
        return new Store(
                storeEntity.getId(),
                storeEntity.getStoreId(),
                storeEntity.getStoreName(),
                storeEntity.getTelNo(),
                storeEntity.getPostNo(),
                storeEntity.getJibunAddr(),
                storeEntity.getRoadAddr(),
                storeEntity.getLat(),
                storeEntity.getLng(),
                storeEntity.getAreaCode(),
                storeEntity.getAreaDetailCode(),
                storeEntity.getCreatedAt(),
                storeEntity.getUpdatedAt()
        );
    }
}