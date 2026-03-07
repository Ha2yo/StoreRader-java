package com.storerader.server.domain.admin.dto.add.goods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

// 공공데이터 상품 API의 개별 상품 정보
@JsonIgnoreProperties(ignoreUnknown = true)
public record GoodApiItem(
        // 상품 ID
        @JacksonXmlProperty(localName = "goodId")
        Integer goodId,

        // 상품 이름
        @JacksonXmlProperty(localName = "goodName")
        String goodName,

        // 상품 용량/수량
        @JacksonXmlProperty(localName = "goodTotalCnt")
        String goodTotalCnt,

        // 상품 구분 코드
        @JacksonXmlProperty(localName = "goodTotalDivCode")
        String goodTotalDivCode
) {}
