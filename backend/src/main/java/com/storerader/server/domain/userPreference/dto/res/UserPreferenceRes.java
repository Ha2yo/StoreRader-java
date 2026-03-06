package com.storerader.server.domain.userPreference.dto.res;

import com.storerader.server.common.entity.UserPreferenceEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 선호도 가중치 응답")
public record UserPreferenceRes(
        @Schema(description = "거리 선호 가중치", example = "0.65")
        double distanceWeight,
        @Schema(description = "가격 선호 가중치", example = "0.35")
        double priceWeight
) {

    public static UserPreferenceRes from(UserPreferenceEntity u) {
        return new UserPreferenceRes(
                u.getDistanceWeight(),
                u.getPriceWeight()
        );
    }
}