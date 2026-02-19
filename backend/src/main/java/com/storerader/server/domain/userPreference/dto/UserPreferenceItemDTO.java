package com.storerader.server.domain.userPreference.dto;

import com.storerader.server.common.entity.UserPreferenceEntity;

public record UserPreferenceItemDTO (
        double distanceWeight,
        double priceWeight
) {
    public static UserPreferenceItemDTO from(UserPreferenceEntity u) {
        return new UserPreferenceItemDTO (
                u.getDistanceWeight(),
                u.getPriceWeight()
        );
    }
}