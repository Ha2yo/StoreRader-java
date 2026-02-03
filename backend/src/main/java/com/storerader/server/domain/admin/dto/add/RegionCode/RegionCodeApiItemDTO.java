package com.storerader.server.domain.admin.dto.add.RegionCode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegionCodeApiItemDTO(
        @JacksonXmlProperty(localName = "code")
        String code,
        @JacksonXmlProperty(localName = "codeName")
        String name,
        @JacksonXmlProperty(localName = "highCode")
        String parentCode
) {}
