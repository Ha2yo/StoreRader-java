package com.storerader.server.admin.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoodApiResponse {
    @JacksonXmlProperty(localName = "result")
    private GoodApiResult result;
}