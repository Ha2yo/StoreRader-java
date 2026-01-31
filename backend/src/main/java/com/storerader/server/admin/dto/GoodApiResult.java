package com.storerader.server.admin.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public record GoodApiResult(
        @JacksonXmlProperty(localName = "item")
        List<GoodApiItem> item
) {}