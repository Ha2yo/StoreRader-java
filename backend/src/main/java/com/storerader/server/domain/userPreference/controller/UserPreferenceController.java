package com.storerader.server.domain.userPreference.controller;

import com.storerader.server.domain.auth.service.AuthService;
import com.storerader.server.domain.userPreference.dto.res.UserPreferenceRes;
import com.storerader.server.domain.userPreference.service.UserPreferenceService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 선호도 API", description = "유저의 선호도 가중치를 관리하는 API")
@RestController
@RequiredArgsConstructor
public class UserPreferenceController {

    private final AuthService authService;
    private final UserPreferenceService userPreferenceService;

    @Operation(
            summary = "유저 선호도 가중치 조회",
            description = "로그인한 유저의 선호도 가중치를 조회합니다."
    )
    @GetMapping("/user-preference")
    public UserPreferenceRes findUserPreference(
            @CookieValue("accessToken") String accessToken
    ) {

        Claims claims = authService.decodeJwt(accessToken);
        Long userId = Long.parseLong(claims.getSubject());

        return userPreferenceService.findUserPreference(userId);
    }
}
