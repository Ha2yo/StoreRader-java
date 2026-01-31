package com.storerader.server.admin.service;

import com.storerader.server.admin.dto.FindAllUsers;
import com.storerader.server.admin.dto.FindAllUsersListResponse;
import com.storerader.server.auth.service.AuthService;
import com.storerader.server.common.entity.UserEntity;
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

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final RestClient restClient;

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

    public String fetchGoodApi(){
        return fetchString(
                "/getProductInfoSvc.do",
                builder -> builder.queryParam("serviceKey", serviceKey),
                "상품"
        );
    }

    private String fetchString(
            String path,
            Function<UriComponentsBuilder, UriComponentsBuilder> queryCustomizer,
            String label
    ) {
        URI uri = queryCustomizer
                .apply(UriComponentsBuilder.fromHttpUrl(BASE_URL).path(path))
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
}
