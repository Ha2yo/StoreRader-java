package com.storerader.server.domain.admin.dto.add.goods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

// 공공데이터 상품 API 전체 응답
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public record GoodApiResponse(

        // API 응답 결과
        @JacksonXmlProperty(localName = "result")
        GoodApiResult result
) {}