package com.storerader.server.domain.admin.dto.add.stores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "response")
public record StoreApiResponseDTO(
        @JacksonXmlProperty(localName = "result")
        StoreApiResultDTO result
) {}