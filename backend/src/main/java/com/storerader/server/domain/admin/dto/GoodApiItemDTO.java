package com.storerader.server.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoodApiItem(
        @JacksonXmlProperty(localName = "goodId")
        String goodId,
        @JacksonXmlProperty(localName = "goodName")
        String goodName,
        @JacksonXmlProperty(localName = "goodTotalCnt")
        String goodTotalCnt,
        @JacksonXmlProperty(localName = "goodTotalDivCode")
        String goodTotalDivCode
) {}
