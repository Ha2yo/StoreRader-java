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
import com.storerader.server.common.repository.GoodRepository;
import com.storerader.server.common.repository.GoodRepositorySQL;
import com.storerader.server.domain.admin.dto.GoodApiItemDTO;
import com.storerader.server.domain.admin.dto.GoodApiResponseDTO;
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
    private final GoodRepository goodRepository;
    private final GoodRepositorySQL goodRepositorySQL;
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

            Integer goodId = parseInteger(item.goodId());
            if (goodId == null) continue;

            GoodEntity good = goodRepository.findByGoodId(goodId)
                    .orElseGet(GoodEntity::new);

            if (good.getCreatedAt() == null) {
                good.setCreatedAt(OffsetDateTime.now());
            }

            good.setGoodId(goodId);
            good.setGoodName(item.goodName());
            good.setTotalCnt(parseInteger(item.goodTotalCnt()));
            good.setTotalDivCode(item.goodTotalDivCode());
            good.setUpdatedAt(OffsetDateTime.now());

            int affected = goodRepositorySQL.upsertGoods(good);
            if (affected > 0)
                applied += affected;

            if (processed % 200 == 0) {
                log.accept("DB 반영 진행 중.. (processed = " + processed + ", applied = " + ")");
            }
        }

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
