package com.storerader.server.domain.store.service;

import com.storerader.server.common.repository.StoreRepository;
import com.storerader.server.domain.store.dto.res.StoreRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     *  전체 매장 목록을 조회한다.
     *
     * @return 매장 정보 리스트
     */
    public List<StoreRes> findAllStores() {
        return storeRepository.findAll()
                .stream()
                .map(StoreRes::from)
                .toList();
    }
}
