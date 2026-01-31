package com.storerader.server.admin.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.storerader.server.admin.dto.FindAllUsers;
import com.storerader.server.admin.dto.FindAllUsersListResponse;
import com.storerader.server.admin.dto.GoodApiItem;
import com.storerader.server.admin.dto.GoodApiResponse;
import com.storerader.server.auth.service.AuthService;
import com.storerader.server.common.entity.GoodEntity;
import com.storerader.server.common.entity.UserEntity;
import com.storerader.server.common.repository.GoodRepository;
import com.storerader.server.common.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final RestClient restClient;
    private final GoodRepository goodRepository;

    private final XmlMapper xmlMapper = new XmlMapper();

    @Value("${PUBLIC_API_KEY}")
    private String serviceKey;

    private static final String BASE_URL =
            "http://openapi.price.go.kr/openApiImpl/ProductPriceInfoService";

    public void requireAdminFromDb(HttpServletRequest request) {
        String accessToken = authService.extractTokenFromCookie(request, "accessToken");

        if(accessToken == null || accessToken.isBlank())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증 토큰이 없습니다.");

        Claims claims = authService.decodeJwt(accessToken);
        long userId;
        try {
            userId = Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "존재하지 않는 유저입니다."));

        if(!"ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
    }

    @Transactional
    public FindAllUsersListResponse findAllUsers() {
        // 1. DB로부터 전 사용자 정보를 조회한다.
        List<UserEntity> userEntities = userRepository.findAll();

        // 2. DTO를 사용하여 원하는 정보만 담는다 (Entity -> DTO 변환).
        List<FindAllUsers> userDtos = userEntities.stream()
                .map(FindAllUsers::from)
                .toList();

        // 3. 최종 DTO(리스트 응답용)를 생성하여 반환한다.
        return new FindAllUsersListResponse(userDtos);
    }

    @Transactional
    public String fetchGoodApi(){
        String xml = fetchString(
                "/getProductInfoSvc.do",
                "상품"
        );

        GoodApiResponse response = parseGoodsResponse(xml);

        if(response == null || response.result() == null)
            return xml;

        for (GoodApiItem item: response.result().item()) {
            Integer goodId = parseInteger(item.goodId());
            GoodEntity good = goodId == null
                    ?new GoodEntity()
                    :goodRepository.findByGoodId(goodId).orElseGet(GoodEntity::new);

            if(good.getCreatedAt() == null)
                good.setCreatedAt(OffsetDateTime.now());

            good.setGoodId(goodId);
            good.setGoodName(item.goodName());
            good.setTotalCnt(parseInteger(item.goodTotalCnt()));

            good.setTotalDivCode(item.goodTotalDivCode());
            good.setUpdatedAt(OffsetDateTime.now());
            goodRepository.save(good);
        }

        return xml;
    }

    private String fetchString(
            String path,
            String label
    ) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(path)
                .queryParam("servicekey", serviceKey)
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

    private GoodApiResponse parseGoodsResponse(String xml) {
        try {
            return xmlMapper.readValue(xml, GoodApiResponse.class);
        } catch (IOException ex) {
            throw new IllegalStateException("상품 응답 파싱 실패: " + ex.getMessage(), ex);
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank())
            return null;
        try {
            return Integer.valueOf(value.trim());
        } catch(NumberFormatException ex) {
            return null;
        }
    }
}
