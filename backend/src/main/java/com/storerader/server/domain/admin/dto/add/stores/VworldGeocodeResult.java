package com.storerader.server.domain.admin.dto.add.stores;

// VWorld API 지오코딩 결과 (위도, 경도)
public record VworldGeocodeResult(
        double lat,
        double lng
) {}
