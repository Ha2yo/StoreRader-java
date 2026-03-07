package com.storerader.server.domain.admin.dto.add.RegionCode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

// 공공데이터 지역코드 API의 개별 지역코드 정보
@JsonIgnoreProperties(ignoreUnknown = true)
public record RegionCodeApiItem(
        // 지역 코드
        @JacksonXmlProperty(localName = "code")
        String code,

        // 지역 이름
        @JacksonXmlProperty(localName = "codeName")
        String name,

        // 상위 코드
        @JacksonXmlProperty(localName = "highCode")
        String parentCode
) {}
