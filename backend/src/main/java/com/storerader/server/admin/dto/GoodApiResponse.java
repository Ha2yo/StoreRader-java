package com.storerader.server.admin.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record GoodApiResponse(
        @JacksonXmlProperty(localName = "resultCode")
        String resultCode,

        @JacksonXmlProperty(localName = "resultMsg")
        String resultMsg,

        @JacksonXmlProperty(localName = "result")
        GoodApiResult result
) {}