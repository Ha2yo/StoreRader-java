package com.storerader.server.domain.admin.dto.add.goods;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoodApiItemDTO(
        @JacksonXmlProperty(localName = "goodId")
        Integer goodId,
        @JacksonXmlProperty(localName = "goodName")
        String goodName,
        @JacksonXmlProperty(localName = "goodTotalCnt")
        String goodTotalCnt,
        @JacksonXmlProperty(localName = "goodTotalDivCode")
        String goodTotalDivCode
) {}
