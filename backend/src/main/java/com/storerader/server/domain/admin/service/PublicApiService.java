/*
 * File: domain/admin/service/publicApiService.java
 * Description:
 *     공공 데이터 포털과 통신을 담당한다
 *
 * Responsibilities:
 *      1) fetchString()
 *          - 공공데이터포털에 HTTP 요청을 보내고 XML 형태로 반환한다
 *
 *      2) parseGoodsResponse()
 *          - 상품 관련 XML 응답을 DTO로 파싱한다
 *
 *      3) saveGoods()
 *          - 파싱된 상품 데이터를 DB에 저장/갱신한다
 */

package com.storerader.server.domain.admin.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.storerader.server.common.entity.GoodEntity;
import com.storerader.server.common.entity.RegionCodeEntity;
import com.storerader.server.common.entity.StoreEntity;
import com.storerader.server.common.repository.sql.GoodRepositorySQL;
import com.storerader.server.common.repository.sql.RegionCodeRepositorySQL;
import com.storerader.server.common.repository.sql.StoreRepositorySQL;
import com.storerader.server.domain.admin.dto.add.RegionCode.RegionCodeApiItemDTO;
import com.storerader.server.domain.admin.dto.add.RegionCode.RegionCodeApiResponseDTO;
import com.storerader.server.domain.admin.dto.add.goods.GoodApiItemDTO;
import com.storerader.server.domain.admin.dto.add.goods.GoodApiResponseDTO;
import com.storerader.server.domain.admin.dto.add.stores.StoreApiItemDTO;
import com.storerader.server.domain.admin.dto.add.stores.StoreApiResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PublicApiService {
    private final GoodRepositorySQL goodRepositorySQL;
    private final StoreRepositorySQL storeRepositorySQL;
    private final RegionCodeRepositorySQL regionCodeRepositorySQL;
    private final VworldService vworldService;
    private final RestClient restClient;
    private final XmlMapper xmlMapper = new XmlMapper();

    @Value("${PUBLIC_API_KEY}")
    private String serviceKey;

    private static final String BASE_URL =
            "http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService";

    @Transactional
    public int saveGoods(
            GoodApiResponseDTO response,
            Consumer<String> log
    ) {
        if (response == null || response.result() == null || response.result().item() == null) {
            log.accept("저장할 데이터가 없습니다.");
            return 0;
        }

        int processed = 0;
        int applied = 0;

        for (GoodApiItemDTO item : response.result().item()) {
            processed++;

            GoodEntity good = new GoodEntity();

            if (good.getCreatedAt() == null) {
                good.setCreatedAt(OffsetDateTime.now());
            }

            good.setGoodId(item.goodId());
            good.setGoodName(item.goodName());
            good.setTotalCnt(parseInteger(item.goodTotalCnt()));
            good.setTotalDivCode(item.goodTotalDivCode());

            good.setUpdatedAt(OffsetDateTime.now());

            int affected = goodRepositorySQL.upsertGoods(good);
            if (affected > 0)
                applied += affected;

            if (processed % 200 == 0) {
                log.accept("DB에 반영 중.. \n(processed = " + processed + ", applied = " + applied + ")");
            }
        }

        log.accept(processed + "개 데이터 처리 완료");

        return applied;
    }

    @Transactional
    public int saveStores(
            StoreApiResponseDTO response,
            Consumer<String> log
    ) {
        if (response == null || response.result() == null || response.result().item() == null) {
            log.accept("저장할 데이터가 없습니다.");
            return 0;
        }

        int processed = 0;
        int applied = 0;
        int geoCodeFail = 0;
        int geoCodeSuccess = 0;

        for (StoreApiItemDTO item : response.result().item()) {

            boolean roadBlank = item.roadAddr() == null || item.roadAddr().isBlank();
            boolean jibunBlank = item.jibunAddr() == null || item.jibunAddr().isBlank();

            if (roadBlank && jibunBlank)
                continue;

            String addr = !roadBlank ? item.roadAddr() : item.jibunAddr();

            processed++;

            var geoCoding = vworldService.geocode(addr, log);
            if (geoCoding.isEmpty()) {
                geoCodeFail++;
                continue;
            }

            double lat = geoCoding.get().lat();
            double lng = geoCoding.get().lng();

            geoCodeSuccess++;

            StoreEntity store = new StoreEntity();

            if (store.getCreatedAt() == null) {
                store.setCreatedAt(OffsetDateTime.now());
            }

            store.setStoreId(item.storeId());
            store.setStoreName(item.storeName());
            store.setTelNo(item.telNo());
            store.setPostNo(item.postNo());
            store.setJibunAddr(item.jibunAddr());
            store.setRoadAddr(item.roadAddr());
            store.setLat(lat);
            store.setLng(lng);
            store.setAreaCode(item.areaCode());
            store.setAreaDetailCode(item.areaDetailCode());

            store.setUpdatedAt(OffsetDateTime.now());

            int affected = storeRepositorySQL.upsertStores(store);
            if (affected > 0)
                applied += affected;

            if (processed % 100 == 0) {
                log.accept("DB에 반영 중.. \n" +
                        "(processed = " + processed + ", applied = " + applied + ",\n" +
                        "geocode success = " + geoCodeSuccess + ", fail = " + geoCodeFail + ")");
            }
        }

        log.accept(processed + "개 데이터 처리 완료 (geocode success = " +
                geoCodeSuccess + ", fail: " + geoCodeFail);

        return applied;
    }

    @Transactional
    public int saveRegionCodes(
            RegionCodeApiResponseDTO response,
            Consumer<String> log
    ) {
        if (response == null || response.result() == null || response.result().item() == null) {
            log.accept("저장할 데이터가 없습니다.");
            return 0;
        }

        int processed = 0;
        int applied = 0;

        for (RegionCodeApiItemDTO item : response.result().item()) {
            processed++;

            RegionCodeEntity regionCode = new RegionCodeEntity();

            regionCode.setCode(item.code());
            regionCode.setName(item.name());
            regionCode.setParentCode(item.parentCode());

            if(item.parentCode().equals("020000000"))
                regionCode.setLevel(1);
            else
                regionCode.setLevel(2);

            int affected = regionCodeRepositorySQL.upsertRegionCodes(regionCode);
            if (affected > 0)
                applied += affected;

            if (processed % 200 == 0) {
                log.accept("DB에 반영 중.. \n(processed = " + processed + ", applied = " + applied + ")");
            }
        }

        log.accept(processed + "개 데이터 처리 완료");

        return applied;
    }


    public String fetchString(
            String path,
            String label
    ) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(path)
                .queryParam("ServiceKey", serviceKey)
                .build(true)
                .toUri();

        try {
            String body = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(String.class);

            if (body == null || body.isBlank()) {
                throw new IllegalStateException(label + " 본문 읽기 실패: 응답 본문이 비어있습니다.");
            }

            return body;
        } catch (RestClientResponseException ex) {
            HttpStatusCode status = ex.getStatusCode();
            throw new IllegalStateException(label + " API 오류 상태: " + status, ex);
        } catch (RestClientException ex) {
            throw new IllegalStateException(label + " API 요청 실패: " + ex.getMessage(), ex);
        }
    }

    public GoodApiResponseDTO parseGoodsResponse(String xml) {
        try {
            return xmlMapper.readValue(xml, GoodApiResponseDTO.class);
        } catch (IOException ex) {
            throw new IllegalStateException("상품 응답 파싱 실패: " + ex.getMessage(), ex);
        }
    }

    public StoreApiResponseDTO parseStoresResponse(String xml) {
        try {
            return xmlMapper.readValue(xml, StoreApiResponseDTO.class);
        } catch (IOException ex) {
            throw new IllegalStateException("매장 응답 파싱 실패: " + ex.getMessage(), ex);
        }
    }

    public RegionCodeApiResponseDTO parseRegionCodesResponse(String xml) {
        try {
            return xmlMapper.readValue(xml, RegionCodeApiResponseDTO.class);
        } catch (IOException ex) {
            throw new IllegalStateException("지역코드 응답 파싱 실패: " + ex.getMessage(), ex);
        }
    }

    public Integer parseInteger(String value) {
        if (value == null || value.isBlank())
            return null;
        try {
            return Integer.valueOf(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
