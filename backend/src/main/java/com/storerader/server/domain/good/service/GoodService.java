package com.storerader.server.domain.good.service;

import com.storerader.server.common.repository.GoodRepository;
import com.storerader.server.domain.good.dto.res.GoodRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodService {
    private final GoodRepository goodRepository;

    /**
     * 전체 상품 목록을 조회한다.
     *
     * @return 상품 정보 리스트
     */
    public List<GoodRes> findAllGoods() {
        return goodRepository.findAll()
                .stream()
                .map(GoodRes::from)
                .toList();
    }
}
