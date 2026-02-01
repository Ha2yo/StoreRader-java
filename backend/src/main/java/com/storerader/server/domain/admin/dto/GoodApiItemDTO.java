package com.storerader.server.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoodApiItemDTO(
        @JacksonXmlProperty(localName = "goodId")
        String goodId,
        @JacksonXmlProperty(localName = "goodName")
        String goodName,
        @JacksonXmlProperty(localName = "goodTotalCnt")
        String goodTotalCnt,
        @JacksonXmlProperty(localName = "goodTotalDivCode")
        String goodTotalDivCode
) {}
