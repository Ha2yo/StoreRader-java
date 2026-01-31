package com.storerader.server.admin.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record GoodApiResponse(
        @JacksonXmlProperty(localName = "result")
        GoodApiResult Apiresult
) {}