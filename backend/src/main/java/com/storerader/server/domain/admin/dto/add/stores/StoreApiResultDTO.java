package com.storerader.server.domain.admin.dto.add.stores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StoreApiResultDTO(
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "iros.openapi.service.vo.entpInfoVO")
        List<StoreApiItemDTO> item
) {}