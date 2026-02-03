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
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class VworldService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${VWORLD_API_KEY:}")
    private String serviceKey;

    public Optional<VworldGeocodeDTO> geocode(
            String addr,
            Consumer<String> log
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
            
            JsonNode point = response.path("result").path("point");
            double x = point.path("x").asDouble(0.0);
            double y = point.path("y").asDouble(0.0);

            return Optional.of(new VworldGeocodeDTO(y, x));

        } catch (Exception e) {
            log.accept("VWorld 지오코딩 실패: " + e.getClass().getSimpleName() + "\naddr=" + addr);
            return Optional.empty();
        }
    }
}
