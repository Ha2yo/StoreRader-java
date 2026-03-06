package com.storerader.server.domain.store.dto.res;

import com.storerader.server.common.entity.StoreEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "매장 정보 응답")
public record StoreRes(
        @Schema(description = "DB PK", example = "1")
        Long id,

        @Schema(description = "매장 ID", example = "786")
        Long storeId,

        @Schema(description = "매장 이름", example = "이마트연수점")
        String storeName,

        @Schema(description = "전화번호", example = "032-820-1234")
        String telNo,

        @Schema(description = "우편번호", example = "21975")
        String postNo,

        @Schema(description = "지번 주소", example = "인천광역시 연수구 동춘동 926-9 이마트")
        String jibunAddr,

        @Schema(description = "도로명 주소", example = "인천광역시 연수구 경원대로 184")
        String roadAddr,

        @Schema(description = "위도", example = "37.404396292")
        Double lat,

        @Schema(description = "경도", example = "126.681271854")
        Double lng,

        @Schema(description = "지역 코드", example = "020700000")
        String areaCode,

        @Schema(description = "지역 상세 코드", example = "020710000")
        String areaDetailCode
) {

    public static StoreRes from(StoreEntity entity) {
        return new StoreRes(
                entity.getId(),
                entity.getStoreId(),
                entity.getStoreName(),
                entity.getTelNo(),
                entity.getPostNo(),
                entity.getJibunAddr(),
                entity.getRoadAddr(),
                entity.getLat(),
                entity.getLng(),
                entity.getAreaCode(),
                entity.getAreaDetailCode()
        );
    }
}