package com.storerader.server.domain.admin.dto.add.stores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.List;

// 공공데이터 매장 API 결과 영역
@JsonIgnoreProperties(ignoreUnknown = true)
public record StoreApiResult(

        // 매장 목록
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "iros.openapi.service.vo.entpInfoVO")
        List<StoreApiItem> item
) {}