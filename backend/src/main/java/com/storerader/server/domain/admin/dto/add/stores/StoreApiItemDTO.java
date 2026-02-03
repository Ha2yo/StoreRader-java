package com.storerader.server.domain.admin.dto.add.stores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StoreApiItemDTO(
        @JacksonXmlProperty(localName = "entpId")
        Long storeId,
        @JacksonXmlProperty(localName = "entpName")
        String storeName,
        @JacksonXmlProperty(localName = "entpTelno")
        String telNo,
        @JacksonXmlProperty(localName = "postNo")
        String postNo,
        @JacksonXmlProperty(localName = "plmkAddrBasic")
        String jibunAddr,
        @JacksonXmlProperty(localName = "roadAddrBasic")
        String roadAddr,
        @JacksonXmlProperty(localName = "xMapCoord")
        Double lat,
        @JacksonXmlProperty(localName = "yMapCoord")
        Double lng,
        @JacksonXmlProperty(localName = "entpAreaCode")
        String areaCode,
        @JacksonXmlProperty(localName = "areaDetailCode")
        String areaDetailCode
) {}
