package com.storerader.server.domain.userSelectionLog.service;

import com.storerader.server.common.entity.UserPreferenceEntity;
import com.storerader.server.common.entity.UserSelectionLogEntity;
import com.storerader.server.common.repository.UserPreferenceRepository;
import com.storerader.server.common.repository.UserSelectionLogRepository;
import com.storerader.server.common.repository.sql.UserPreferenceSQL;
import com.storerader.server.common.repository.sql.UserSelectionLogSQL;
import com.storerader.server.domain.userSelectionLog.dto.UserSelectionLogReqDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSelectionLogService {
    private final UserSelectionLogSQL userSelectionLogReqDTO;
    private final UserSelectionLogRepository userSelectionLogRepostiory;
    private final UserPreferenceSQL userPreferenceSQL;
    private final UserPreferenceRepository userPreferenceRepository;

    @Transactional
    public void updateUserSelectionLog(
            Long userId,
            UserSelectionLogReqDTO req
    ) {
        userSelectionLogReqDTO.insertUserSelectionLog(userId, req);

        int selection_count = userPreferenceSQL.increaseSelectionCount(userId);

        if (selection_count % 10 != 0)
            return;

        UserPreferenceEntity oldPreference = userPreferenceRepository.findById(userId).orElseThrow();;
        List<UserSelectionLogEntity> logs = userSelectionLogRepostiory.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        Long priceFocus = logs.stream()
                .filter(l -> "price".equals(l.getPreferenceType()))
                .count();

        double priceRatio = priceFocus / 10.0;
        double newPriceWeightRaw = 0.2 + priceRatio * 0.6;

        // 과거 가중치와 혼합
        double alpha = 0.2;
        double priceWeight = oldPreference.getPriceWeight() * (1.0 - alpha) + newPriceWeightRaw * alpha;

        priceWeight = Math.round(priceWeight * 1000.0) / 1000.0;
        double distanceWeight =  Math.round((1.0 - priceWeight) * 1000.0) / 1000.0;

        userPreferenceSQL.updateUserWeights(userId, priceWeight, distanceWeight);
    }
}
