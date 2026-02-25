package com.storerader.server.domain.userSelectionLog.dto;

public record UserSelectionLogReqDTO  (
        Long storeId,
        Long goodId,
        Integer price,
        String preferenceType
) {}
