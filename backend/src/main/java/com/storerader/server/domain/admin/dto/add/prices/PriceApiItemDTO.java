package com.storerader.server.domain.admin.dto.add.prices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PriceApiItemDTO(
        @JacksonXmlProperty(localName = "goodInspectDay")
        String inspectDay,
        @JacksonXmlProperty(localName = "entpId")
        Integer storeId,
        @JacksonXmlProperty(localName = "goodId")
        Integer goodId,
        @JacksonXmlProperty(localName = "goodPrice")
        Integer price,
        @JacksonXmlProperty(localName = "plusOneYn")
        String isOnePlusOne,
        @JacksonXmlProperty(localName = "goodDnYn")
        String isDiscount,
        @JacksonXmlProperty(localName = "goodDcStartDay")
        String discountStart,
        @JacksonXmlProperty(localName = "goodDcEndDay")
        String discountEnd
) {}
