package com.storerader.server.admin.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record GoodApiItem(
        @JacksonXmlProperty(localName = "goodId") String goodId,
        @JacksonXmlProperty(localName = "goodName") String goodName,
        @JacksonXmlProperty(localName = "goodTotalCnt") String goodTotalCnt,
        @JacksonXmlProperty(localName = "goodTotalDivCode") String goodTotalDivCode
) {}
