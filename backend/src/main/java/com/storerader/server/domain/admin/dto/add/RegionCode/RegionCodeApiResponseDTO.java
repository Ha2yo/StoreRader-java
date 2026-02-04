package com.storerader.server.domain.admin.dto.add.RegionCode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "iros.openapi.service.vo.stdInfoVO")
public record RegionCodeApiResponseDTO(
        @JacksonXmlProperty(localName = "result")
        RegionCodeApiResultDTO result
) {}