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

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PublicApiService publicApiService;

    /**
     *  전체 유저 목록을 조회하여 관리자용 응답 DTO로 반환한다
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
     *  공공 API로부터 상품 데이터를 조회하고 저장한다
     * @return 공공 API로부터 수신한 원본 XML 문자열
     */
    public String fetchGoodApi() {
        String xml = publicApiService.fetchString(
                "/getProductInfoSvc.do",
                "상품"
        );

        GoodApiResponseDTO response = publicApiService.parseGoodsResponse(xml);

        if (response == null || response.result() == null)
            return xml;

        publicApiService.saveGoods(response);
        return xml;
    }
}
