/*
 * File: domain/admin/controller/AdminAuthorzationService.java
 * Description:
 *     관리자(admin) 권한 검증을 전담한다
 *
 * Responsibilities:
 *      1) requireAdmin()
 *          - userId를 기준으로 DB에서 유저를 조회한다
 *          - 해당 유저의 권한을 검증한다
 */

package com.storerader.server.domain.admin.service;

import com.storerader.server.common.entity.UserEntity;
import com.storerader.server.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminAuthorizationService {

    private final UserRepository userRepository;

    /**
     * 주어진 userId가 관리자 권한을 보유하고 있는지 검증한다
     * @param userId DB 상의 유저 ID
     */
    public void requireAdmin(long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "존재하지 않는 유저입니다."));

        if (!"ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
    }
}
