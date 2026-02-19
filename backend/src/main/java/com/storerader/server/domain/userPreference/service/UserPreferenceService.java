package com.storerader.server.domain.userPreference.service;

import com.storerader.server.common.repository.sql.UserPreferenceSQL;
import com.storerader.server.domain.store.dto.FindAllStoreResponseDTO;
import com.storerader.server.domain.store.dto.StoreItemDTO;
import com.storerader.server.domain.userPreference.dto.UserPreferenceItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {
    private final UserPreferenceSQL userPreferenceSQL;

    public UserPreferenceItemDTO findUserPreference(Long userId) {
        var userPreferences = userPreferenceSQL.findPreferenceByUserId(userId)
                .stream()
                .map(StoreItemDTO::from)
                .toList();

        return FindAllStoreResponseDTO.of(stores);
    }
}
