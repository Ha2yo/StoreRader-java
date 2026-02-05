package com.storerader.server.domain.store.controller;

import com.storerader.server.domain.store.dto.FindAllStoreResponseDTO;
import com.storerader.server.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping("/find/store/all")
    public FindAllStoreResponseDTO findAllStores() {
        return storeService.findAllStores();
    }
}
