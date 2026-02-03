package com.storerader.server.domain.admin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storerader.server.domain.admin.dto.add.stores.VworldGeocodeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VworldService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${VWORLD_API_KEY:}")
    private String serviceKey;

    public Optional<VworldGeocodeDTO> geocode(
            String addr
    ) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl("https://api.vworld.kr/req/address?service=address")
                    .queryParam("request", "getCoord")
                    .queryParam("version", "2.0")
                    .queryParam("crs", "epsg:4326")
                    .queryParam("address", addr)
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

            if ("NOT_FOUND".equals(status))
                return Optional.empty();

            if ("ERROR".equals(status)) {
                JsonNode err = response.path("error");
                String code = err.path("code").asText("UNKNOWN ERROR");
                String text = err.path("text").asText("알 수 없는 에러가 발생하였습니다");
                throw new IllegalStateException("vWorld ERROR [" + code + "]: " + text);
            }

            JsonNode point = response.path("result").path("point");
            double x = point.path("x").asDouble(0.0);
            double y = point.path("y").asDouble(0.0);

            if (x == 0.0 && y == 0.0)
                return Optional.empty();

            return Optional.of(new VworldGeocodeDTO(y, x));

        } catch (Exception e) {
            throw new IllegalStateException("Vworld 지오코딩 실패: " + addr, e);
        }
    }
}
