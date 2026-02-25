package com.storerader.server.domain.userSelectionLog.dto;

public record UserSelectionLogReqDTO  (
        Long storeId,
        String goodId,
        Integer price,
        String preferenceType
) {}
