package com.storerader.server.domain.admin.dto.add.prices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

// 공공데이터 가격 API의 개별 가격 정보
@JsonIgnoreProperties(ignoreUnknown = true)
public record PriceApiItem(
        // 상품가격 조사일자
        @JacksonXmlProperty(localName = "goodInspectDay")
        String inspectDay,

        // 매장 ID
        @JacksonXmlProperty(localName = "entpId")
        Long storeId,

        // 상품 ID
        @JacksonXmlProperty(localName = "goodId")
        Integer goodId,

        // 상품 가격
        @JacksonXmlProperty(localName = "goodPrice")
        Integer price,

        // 1+1 여부
        @JacksonXmlProperty(localName = "plusOneYn")
        String isOnePlusOne,

        // 할인 여부
        @JacksonXmlProperty(localName = "goodDnYn")
        String isDiscount,

        // 할인 시작일자
        @JacksonXmlProperty(localName = "goodDcStartDay")
        String discountStart,

        // 할인 종료일자
        @JacksonXmlProperty(localName = "goodDcEndDay")
        String discountEnd
) {}
