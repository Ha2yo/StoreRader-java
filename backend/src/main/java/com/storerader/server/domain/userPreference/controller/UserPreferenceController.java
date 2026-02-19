package com.storerader.server.domain.userPreference.controller;

import com.storerader.server.domain.auth.service.AuthService;
import com.storerader.server.domain.store.dto.FindAllStoreResponseDTO;
import com.storerader.server.domain.store.service.StoreService;
import com.storerader.server.domain.userPreference.dto.UserPreferenceItemDTO;
import com.storerader.server.domain.userPreference.service.UserPreferenceService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserPreferenceController {
    private final AuthService authService;
    private final UserPreferenceService userPreferenceService;

    @GetMapping("/find/user-preference")
    public UserPreferenceItemDTO findUserPreference(
            @CookieValue("accsesToken") String accessToken
    ) {

        Claims claims = authService.decodeJwt(accessToken);
        Long userId = Long.parseLong(claims.getSubject());

        return userPreferenceService.findUserPreference(userId);
    }
}
