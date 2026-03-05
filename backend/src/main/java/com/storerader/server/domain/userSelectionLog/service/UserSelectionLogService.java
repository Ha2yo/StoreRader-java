package com.storerader.server.domain.userSelectionLog.service;

import com.storerader.server.common.entity.UserPreferenceEntity;
import com.storerader.server.common.entity.UserSelectionLogEntity;
import com.storerader.server.common.repository.UserPreferenceRepository;
import com.storerader.server.common.repository.UserSelectionLogRepository;
import com.storerader.server.common.repository.sql.UserPreferenceSQL;
import com.storerader.server.common.repository.sql.UserSelectionLogSQL;
import com.storerader.server.domain.userSelectionLog.dto.req.UserSelectionLogReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSelectionLogService {

    private final UserSelectionLogSQL userSelectionLogReqDTO;
    private final UserPreferenceSQL userPreferenceSQL;
    private final UserSelectionLogRepository userSelectionLogRepostiory;
    private final UserPreferenceRepository userPreferenceRepository;

    /**
     * 유저의 매장 선택 로그를 저장하고, 선택 횟수를 누적한다.
     *
     * <p>선택 횟수가 10회 단위로 누적될 때마다 최근 10개의 선택 로그를 기반으로
     * 가격/거리 선호 가중치를 재계산하여 사용자 선호 가중치를 갱신한다.</p>
     *
     * @param userId DB 상의 유저 ID
     * @param req 선택 로그 요청 데이터
     */
    @Transactional
    public void updateUserSelectionLog(
            Long userId,
            UserSelectionLogReq req
    ) {
        userSelectionLogReqDTO.insertUserSelectionLog(userId, req);

        int selection_count = userPreferenceSQL.increaseSelectionCount(userId);

        // 선택 횟수가 10회 단위일 때만 가중치 재계산
        if (selection_count % 10 != 0)
            return;

        UserPreferenceEntity oldPreference = userPreferenceRepository.findById(userId).orElseThrow();

        List<UserSelectionLogEntity> logs = userSelectionLogRepostiory.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        // 최근 10회 중 price 선택 비율 계산
        long priceFocus = logs.stream()
                .filter(l -> "price".equals(l.getPreferenceType()))
                .count();

        double priceRatio = priceFocus / 10.0;
        double newPriceWeightRaw = 0.2 + priceRatio * 0.6;

        // 기존 가중치와 새 계산값을 alpha 비율로 혼합
        double alpha = 0.2;
        double priceWeight = oldPreference.getPriceWeight() * (1.0 - alpha) + newPriceWeightRaw * alpha;

        priceWeight = Math.round(priceWeight * 1000.0) / 1000.0;
        double distanceWeight = Math.round((1.0 - priceWeight) * 1000.0) / 1000.0;

        userPreferenceSQL.updateUserWeights(userId, priceWeight, distanceWeight);
    }
}