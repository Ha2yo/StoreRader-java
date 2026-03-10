package com.storerader.server.domain.userPreference.service;

import com.storerader.server.common.exception.CustomException;
import com.storerader.server.common.exception.ExceptionClass;
import com.storerader.server.common.repository.sql.UserPreferenceSQL;
import com.storerader.server.domain.userPreference.dto.res.UserPreferenceRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceSQL userPreferenceSQL;

    /**
     * 특정 유저의 선호도 가중치를 조회한다.
     * @param userId 조회할 유저 ID
     * @return 거리/가격 선호 가중치 정보
     */
    public UserPreferenceRes findUserPreference(
            Long userId
    ) {
        UserPreferenceRes res = userPreferenceSQL.findPreferenceByUserId(userId);

        if (res == null) {
            throw new CustomException(ExceptionClass.USER_NOT_FOUND);
        }

        return res;
    }
}