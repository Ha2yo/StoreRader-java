package com.storerader.server.domain.admin.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.storerader.server.common.entity.GoodEntity;
import com.storerader.server.common.entity.PriceEntity;
import com.storerader.server.common.entity.RegionCodeEntity;
import com.storerader.server.common.entity.StoreEntity;
import com.storerader.server.common.exception.CustomException;
import com.storerader.server.common.exception.ExceptionClass;
import com.storerader.server.common.repository.sql.GoodRepositorySQL;
import com.storerader.server.common.repository.sql.PriceRepositorySQL;
import com.storerader.server.common.repository.sql.RegionCodeRepositorySQL;
import com.storerader.server.common.repository.sql.StoreRepositorySQL;
import com.storerader.server.domain.admin.dto.add.RegionCode.RegionCodeApiItem;
import com.storerader.server.domain.admin.dto.add.RegionCode.RegionCodeApiResponse;
import com.storerader.server.domain.admin.dto.add.goods.GoodApiItem;
import com.storerader.server.domain.admin.dto.add.goods.GoodApiResponse;
import com.storerader.server.domain.admin.dto.add.prices.PriceApiItem;
import com.storerader.server.domain.admin.dto.add.prices.PriceApiResponse;
import com.storerader.server.domain.admin.dto.add.stores.StoreApiItem;
import com.storerader.server.domain.admin.dto.add.stores.StoreApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 공공데이터 포털 가격정보 API 연동 서비스
 * 외부 공공 API에서 상품, 매장, 지역코드, 가격 정보를 조회한 뒤
 * DB에 저장한다.
 */
@Service
@RequiredArgsConstructor
public class PublicApiService {

    private final GoodRepositorySQL goodRepositorySQL;
    private final StoreRepositorySQL storeRepositorySQL;
    private final RegionCodeRepositorySQL regionCodeRepositorySQL;
    private final PriceRepositorySQL priceRepositorySQL;
    private final VworldService vworldService;
    private final RestClient restClient;
    private final XmlMapper xmlMapper = new XmlMapper();

    @Value("${PUBLIC_API_KEY}")
    private String serviceKey;

    // 공공데이터 가격정보 API 기본 URL
    private static final String BASE_URL =
            "http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService";

    /**
     * 상품 데이터를 DB에 저장한다.
     * API 응답의 상품 목록을 순회하며 반영한다.
     *
     * @param response 상품 API 응답 객체
     * @param log      처리 과정 로그 콜백
     * @return 실제 DB에 반영된 row 수
     */
    @Transactional
    public int saveGoods(
            GoodApiResponse response,
            Consumer<String> log
    ) {
        if (response == null || response.result() == null || response.result().item() == null) {
            log.accept("저장할 데이터가 없습니다.");
            return 0;
        }

        int processed = 0;
        int applied = 0;

        for (GoodApiItem item : response.result().item()) {
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

    /**
     * 매장 데이터를 DB에 저장한다.
     * 주소 정보를 기반으로 VWorld 지오코딩을 수행하여
     * 위도/경도 좌표를 계산한 뒤 저장한다.
     *
     * @param response 매장 API 응답 객체
     * @param log      처리 과정 로그 콜백
     * @return 실제 DB에 반영된 row 수
     */
    @Transactional
    public int saveStores(
            StoreApiResponse response,
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

        for (StoreApiItem item : response.result().item()) {

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

    /**
     * 지역 코드 데이터를 DB에 저장한다.
     * 부모 코드 값을 기준으로 행정구역 레벨을 구분하여 저장한다.
     *
     * @param response 지역 코드 API 응답 객체
     * @param log      처리 과정 로그 콜백
     * @return 실제 DB에 반영된 row 수
     */
    @Transactional
    public int saveRegionCodes(
            RegionCodeApiResponse response,
            Consumer<String> log
    ) {
        if (response == null || response.result() == null || response.result().item() == null) {
            log.accept("저장할 데이터가 없습니다.");
            return 0;
        }

        int processed = 0;
        int applied = 0;

        for (RegionCodeApiItem item : response.result().item()) {
            processed++;

            RegionCodeEntity regionCode = new RegionCodeEntity();

            regionCode.setCode(item.code());
            regionCode.setName(item.name());
            regionCode.setParentCode(item.parentCode());

            if (item.parentCode().equals("020000000"))
                regionCode.setLevel(1);
            else
                regionCode.setLevel(2);

            int affected = regionCodeRepositorySQL.upsertRegionCodes(regionCode);
            if (affected > 0)
                applied += affected;

            if (processed % 100 == 0) {
                log.accept("DB에 반영 중.. \n(processed = " + processed + ", applied = " + applied + ")");
            }
        }

        log.accept(processed + "개 데이터 처리 완료");

        return applied;
    }

    /**
     * 가격 데이터를 DB에 저장한다.
     * API 응답의 가격 목록을 순회하며 반영한다.
     *
     * @param response 가격 API 응답 객체
     * @param log      처리 과정 로그 콜백
     * @return 실제 DB에 반영된 row 수
     */
    public int savePrices(
            PriceApiResponse response,
            Consumer<String> log
    ) {
        if (response == null || response.result() == null || response.result().item() == null) {
            log.accept("저장할 데이터가 없습니다.");
            return 0;
        }

        boolean started = false;
        int processed = 0;
        int applied = 0;
        int total = 0;

        for (PriceApiItem item : response.result().item()) {
            processed++;

            PriceEntity price = new PriceEntity();

            if (price.getCreatedAt() == null) {
                price.setCreatedAt(OffsetDateTime.now());
            }

            price.setInspectDay(item.inspectDay());
            price.setStoreId(item.storeId());
            price.setGoodId(item.goodId());
            price.setPrice(item.price());
            price.setIsOnePlusOne(item.onePlusOne());
            price.setIsDiscount(item.discount());
            price.setDiscountStart(item.discountStart());
            price.setDiscountEnd(item.discountEnd());

            if (!started) {
                log.accept("DB에 반영 중..");
                started = true;
            }

            int affected = priceRepositorySQL.upsertPrices(price);
            if (affected > 0) applied += affected;
        }

        log.accept(processed + "개 데이터 처리 완료");

        return applied;
    }

    /**
     * 공공 API에 요청을 보내고 XML 문자열 응답을 반환한다.
     *
     * @param path   API 경로
     * @param label  로그용 API 이름
     * @param params 추가 query parameter
     * @return API 응답 XML 문자열
     */
    public String fetchString(
            String path,
            String label,
            Map<String, String> params
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(path)
                .queryParam("ServiceKey", serviceKey);

        params.forEach(builder::queryParam);

        URI uri = builder.build(true).toUri();

        try {
            String body = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(String.class);

            if (body == null || body.isBlank()) {
                throw new CustomException(ExceptionClass.EXTERNAL_API_ERROR);
            }

            return body;
        } catch (Exception e) {
            throw new CustomException(ExceptionClass.EXTERNAL_API_ERROR);
        }
    }

    /**
     * 공공 AI에 요청을 보내고 XML 문자열 응답을 반환한다.
     * 추가 파라미터가 없는 경우 사용한다.
     *
     * @param path  API 경로
     * @param label 로그용 API 이름
     * @return 응답 XML 문자열
     */
    public String fetchString(
            String path,
            String label
    ) {
        return fetchString(path, label, Map.of());
    }

    /**
     * XML 문자열을 지정한 DTO 타입으로 변환한다.
     *
     * @param xml   파싱할 XML 문자열
     * @param clazz 변환할 DTO 클래스 타입
     * @param label 예외 메시지에 이용할 API 이름
     * @return 변환된 DT 객체
     */
    public <T> T parseXML(String xml, Class<T> clazz, String label) {
        try {
            return xmlMapper.readValue(xml, clazz);
        } catch (IOException ex) {
            throw new IllegalStateException(label + " 응답 파싱 실패: " + ex.getMessage(), ex);
        }
    }

    /**
     * 문자열을 Integer로 변환한다.
     *
     * @param value 변환할 문자열
     * @return Integer 값 또는 null
     */
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
