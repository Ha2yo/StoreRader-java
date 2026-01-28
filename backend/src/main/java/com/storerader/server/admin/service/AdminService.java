package com.storerader.server.admin.service;

import com.storerader.server.admin.dto.FindAllUsers;
import com.storerader.server.admin.dto.FindAllUsersListResponse;
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
}
