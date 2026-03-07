package com.storerader.server.domain.admin.dto.add.RegionCode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

// 공공데이터 지역코드 API 결과 영역
@JsonIgnoreProperties(ignoreUnknown = true)
public record RegionCodeApiResult(

        // 지역코드 목록
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "iros.openapi.service.vo.stdInfoVO")
        List<RegionCodeApiItem> item
) {}