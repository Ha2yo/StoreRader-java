package com.storerader.server.domain.admin.dto.add.goods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

// 공공데이터 상품 API 결과 영역
@JsonIgnoreProperties(ignoreUnknown = true)
public record GoodApiResult(

        // 상품 목록
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "item")
        List<GoodApiItem> item
) {}