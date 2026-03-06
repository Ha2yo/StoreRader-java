package com.storerader.server.domain.regioncode.service;

import com.storerader.server.common.repository.RegionCodeRepository;
import com.storerader.server.domain.regioncode.dto.res.RegionCodeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionCodeService {

    private final RegionCodeRepository regionCodeRepository;

    /**
     * 전체 지역 코드를 조회한다.
     *
     * @return 지역 코드 리스트
     */
    public List<RegionCodeRes> findAllRegionCodes() {
        return regionCodeRepository.findAll()
                .stream()
                .map(RegionCodeRes::from)
                .toList();
    }
}