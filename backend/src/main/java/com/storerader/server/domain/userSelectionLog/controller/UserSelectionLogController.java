package com.storerader.server.domain.userSelectionLog.controller;

import com.storerader.server.domain.auth.service.AuthService;
import com.storerader.server.domain.userSelectionLog.dto.req.UserSelectionLogReq;
import com.storerader.server.domain.userSelectionLog.service.UserSelectionLogService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 선택 로그 API", description = "유저의 매장 선택 로그를 관리하는 API")
@RestController
@RequiredArgsConstructor
public class UserSelectionLogController {

    private final AuthService authService;
    private final UserSelectionLogService userSelectionLogService;

    /**
     * 로그인한 유저의 매장 선택 로그를 저장한다.
     * 쿠키에 포함된 accessToken을 통해 사용자 ID를 추출한 후,
     * 선택 로그를 기록하고 필요 시 선호 가중치를 갱신한다.
     *
     * @param accessToken 로그인 유저 JWT (쿠키)
     * @param req 선택 로그 요청 데이터
     */
    @Operation(
            summary = "유저 선택 로그 저장",
            description = "로그인 유저의 매장 선택 정보를 저장하고, "
                    + "일정 횟수 도달 시 선호 가중치를 자동 갱신합니다."
    )
    @PostMapping("/user-selection")
    public void updateUserSelectionLog(
            @CookieValue("accessToken") String accessToken,
            @RequestBody UserSelectionLogReq req
    ) {
        Claims claims = authService.decodeJwt(accessToken);
        Long userId = Long.parseLong(claims.getSubject());

        userSelectionLogService.updateUserSelectionLog(userId, req);
    }
}