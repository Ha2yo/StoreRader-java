package com.storerader.server.domain.admin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storerader.server.domain.admin.dto.add.stores.VworldGeocodeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * VWorld 주소 좌표 변환 서비스
 * 입력된 주소를 VWorld 주소 검색 API에 전달하여
 * 위도/경도 좌표로 변환한다.
 */
@Component
@RequiredArgsConstructor
public class VworldService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${VWORLD_API_KEY:}")
    private String serviceKey;

    /**
     * 주소를 위도/경도 좌표로 변환한다.
     *
     * @param addr 변환할 도로명 주소
     * @param log 처리 중 로그 메시지를 전달할 콜백
     * @return 변환 성공 시 좌표 정보, 실패 시
     */
    public Optional<VworldGeocodeResult> geocode(
            String addr,
            Consumer<String> log
    ) {
        try {
            // Vworld 좌표 변환 API 호출
            URI uri = UriComponentsBuilder.fromHttpUrl("https://api.vworld.kr/req/address")
                    .queryParam("service", "address")
                    .queryParam("request", "getCoord")
                    .queryParam("version", "2.0")
                    .queryParam("crs", "epsg:4326")
                    .queryParam("address", URLEncoder.encode(addr, StandardCharsets.UTF_8))
                    .queryParam("refine", "true")
                    .queryParam("simple", "false")
                    .queryParam("type", "road")
                    .queryParam("key", serviceKey)
                    .build(true)
                    .toUri();

            String body = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(String.class);

            if (!StringUtils.hasText(body))
                return Optional.empty();

            JsonNode json = objectMapper.readTree(body);
            JsonNode response = json.path("response");
            String status = response.path("status").asText("");

            if (!"OK".equals(status))
                return Optional.empty();

            // 좌표 추출 (x=경도, y=위도)
            JsonNode point = response.path("result").path("point");
            double x = point.path("x").asDouble(0.0);
            double y = point.path("y").asDouble(0.0);

            return Optional.of(new VworldGeocodeResult(y, x));

        } catch (Exception e) {
            // 실패 시 로그 출력 후 skip
            log.accept("VWorld 지오코딩 실패: " + e.getClass().getSimpleName() + "\naddr=" + addr);
            return Optional.empty();
        }
    }
}
