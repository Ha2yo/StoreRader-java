package com.storerader.server.domain.userSelectionLog.controller;

import com.storerader.server.domain.auth.service.AuthService;
import com.storerader.server.domain.userPreference.dto.UserPreferenceItemDTO;
import com.storerader.server.domain.userSelectionLog.dto.UserSelectionLogReqDTO;
import com.storerader.server.domain.userSelectionLog.service.UserSelectionLogService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserSelectionLogController {
    private final AuthService authService;
    private final UserSelectionLogService userSelectionLogService;

    @GetMapping("/find/user-selection")
    public void updateUserSelectionLog(
            @CookieValue("accessToken") String accessToken,
            UserSelectionLogReqDTO req
    ) {

        Claims claims = authService.decodeJwt(accessToken);
        Long userId = Long.parseLong(claims.getSubject());

        userSelectionLogService.updateUserSelectionLog(userId, req);
    }
}
