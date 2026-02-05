package com.storerader.server.domain.store.service;

import com.storerader.server.common.repository.StoreRepository;
import com.storerader.server.domain.store.dto.FindAllStoreResponseDTO;
import com.storerader.server.domain.store.dto.StoreItemDTO;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.Lint;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public FindAllStoreResponseDTO findAllStores() {
        var stores = storeRepository.findAll()
                .stream()
                .map(StoreItemDTO::from)
                .toList();

        return FindAllStoreResponseDTO.of(stores);
    }
}
