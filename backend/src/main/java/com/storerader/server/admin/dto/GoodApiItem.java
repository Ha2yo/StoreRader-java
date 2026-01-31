package com.storerader.server.admin.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoodApiItem {
    @JacksonXmlProperty(localName = "goodId")
    private String goodId;

    @JacksonXmlProperty(localName = "goodName")
    private String goodName;

    @JacksonXmlProperty(localName = "goodTotalCnt")
    private String goodTotalCnt;

    @JacksonXmlProperty(localName = "goodTotalDivCode")
    private String goodTotalDivCode;
}