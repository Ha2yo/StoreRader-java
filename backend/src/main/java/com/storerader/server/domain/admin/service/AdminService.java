/*
 * File: domain/admin/service/AdminService.java
 * Description:
 *     관리자(admin) 도메인의 서비스 계층으로,
 *     관리자 전용 기능의 비즈니스 로직을 수행한다.
 *
 * Responsibilities:
 *      1) findAllUsers()
 *          - 전체 유저 목록을 조회하여 관리자용 DTO로 반환한다
 *
 *      2) fetchGoodApi()
 *          - 공공 API로부터 상품 데이터를 조회하고 저장한다
 */

package com.storerader.server.domain.admin.service;

import com.storerader.server.domain.admin.dto.FindAllUsersDTO;
import com.storerader.server.domain.admin.dto.FindAllUsersListResponseDTO;
import com.storerader.server.domain.admin.dto.GoodApiResponseDTO;
import com.storerader.server.common.entity.UserEntity;
import com.storerader.server.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PublicApiService publicApiService;

    /**
     * 전체 유저 목록을 조회하여 관리자용 응답 DTO로 반환한다
     *
     * @return 유저 목록
     */
    @Transactional
    public FindAllUsersListResponseDTO findAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        List<FindAllUsersDTO> userDtos = userEntities.stream()
                .map(FindAllUsersDTO::from)
                .toList();

        return new FindAllUsersListResponseDTO(userDtos);
    }

    /**
     * 공공 API로부터 상품 데이터를 조회하고 저장한다
     *
     * @return 공공 API로부터 수신한 원본 XML 문자열
     */
    public SseEmitter fetchGoodApi() {
        SseEmitter emitter = new SseEmitter(0L);

        new Thread(() -> {
            try {
                Consumer<String> log = msg -> safeSend(emitter, msg);

                log.accept("상품 데이터 추가 시작");

                String xml = publicApiService.fetchString(
                        "/getProductInfoSvc.do",
                        "상품"
                );
                log.accept("공공데이터 응답 수신 완료 (xml length = " + xml.length() + ")");

                GoodApiResponseDTO parsed = publicApiService.parseGoodsResponse(xml);
                int count = parsed.result().item() == null ? 0 : parsed.result().item().size();
                log.accept("XML 파싱 완료 (items = " + count + ")");

                int saved = publicApiService.saveGoods(parsed, log);
                log.accept("DB 반영 완료 (applied = " + saved + ")");

                log.accept("상품 데이터 추가 완료");
                emitter.complete();
            } catch (Exception e) {
                safeSend(emitter, "오류: " + e.getMessage());
                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    private void safeSend(SseEmitter emitter, String msg) {
        try {
            emitter.send(SseEmitter.event().name("log").data(msg));
        } catch (IOException ignored) {
            emitter.complete();
        }
    }
}
