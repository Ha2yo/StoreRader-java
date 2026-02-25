package com.storerader.server.domain.userSelectionLog.dto;

public record UserSelectionLogReqDTO  (
        Long storeId,
        Number goodId,
        Integer price,
        String preferenceType
) {}
