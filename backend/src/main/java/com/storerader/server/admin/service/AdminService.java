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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final AuthService authService;

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


}
