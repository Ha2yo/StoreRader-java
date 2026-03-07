package com.storerader.server.domain.admin.dto.add.stores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

// 공공데이터 매장 API의 개별 매장 정보
@JsonIgnoreProperties(ignoreUnknown = true)
public record StoreApiItem(
        // 매장 ID
        @JacksonXmlProperty(localName = "entpId")
        Long storeId,

        // 매장 이름
        @JacksonXmlProperty(localName = "entpName")
        String storeName,

        // 전화번호
        @JacksonXmlProperty(localName = "entpTelno")
        String telNo,

        // 우편번호
        @JacksonXmlProperty(localName = "postNo")
        String postNo,

        // 지번 주소
        @JacksonXmlProperty(localName = "plmkAddrBasic")
        String jibunAddr,

        // 도로명 주소
        @JacksonXmlProperty(localName = "roadAddrBasic")
        String roadAddr,

        // 위도 좌표
        @JacksonXmlProperty(localName = "xMapCoord")
        Double lat,

        // 경도 좌표
        @JacksonXmlProperty(localName = "yMapCoord")
        Double lng,

        // 지역코드
        @JacksonXmlProperty(localName = "entpAreaCode")
        String areaCode,

        // 상세지역코드
        @JacksonXmlProperty(localName = "areaDetailCode")
        String areaDetailCode
) {}
