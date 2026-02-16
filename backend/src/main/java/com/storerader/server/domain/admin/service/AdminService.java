/*
 * File: domain/admin/service/AdminService.java
 * Description:
 *     관리자(admin) 도메인의 서비스 계층으로,
 *     관리자 전용 기능의 비즈니스 로직을 수행한다.
 *
 * Responsibilities:
 *      1) findAllUsers()
 *          - 전체 유저 목록을 조회하여 관리자용 DTO로 반환한다
 *
 *      2) fetchGoodApi()
 *          - 공공 API로부터 상품 데이터를 조회하고 저장한다
 */

package com.storerader.server.domain.admin.service;

import com.storerader.server.common.entity.*;
import com.storerader.server.common.repository.*;
import com.storerader.server.domain.admin.dto.add.RegionCode.RegionCodeApiResponseDTO;
import com.storerader.server.domain.admin.dto.add.goods.GoodApiResponseDTO;
import com.storerader.server.domain.admin.dto.add.prices.PriceApiResponseDTO;
import com.storerader.server.domain.admin.dto.add.stores.StoreApiResponseDTO;
import com.storerader.server.domain.admin.dto.select.goods.FindAllGoodsDTO;
import com.storerader.server.domain.admin.dto.select.goods.FindAllGoodsListDTO;
import com.storerader.server.domain.admin.dto.select.prices.FindAllPricesDTO;
import com.storerader.server.domain.admin.dto.select.prices.FindAllPricesListDTO;
import com.storerader.server.domain.admin.dto.select.regionCodes.FindAllRegionCodesDTO;
import com.storerader.server.domain.admin.dto.select.regionCodes.FindAllRegionCodesListDTO;
import com.storerader.server.domain.admin.dto.select.stores.FindAllStoresDTO;
import com.storerader.server.domain.admin.dto.select.stores.FindAllStoresListDTO;
import com.storerader.server.domain.admin.dto.select.users.FindAllUsersDTO;
import com.storerader.server.domain.admin.dto.select.users.FindAllUsersListDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;
    private final GoodRepository goodRepository;
    private final StoreRepository storeRepository;
    private final PriceRepository priceRepository;

    private final RegionCodeRepository regionCodeRepository;
    private final PublicApiService publicApiService;

    /**
     * 전체 유저 목록을 조회하여 관리자용 응답 DTO로 반환한다
     *
     * @return 유저 목록
     */
    @Transactional
    public FindAllUsersListDTO findAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        List<FindAllUsersDTO> userDtos = userEntities.stream()
                .map(FindAllUsersDTO::from)
                .toList();

        return new FindAllUsersListDTO(userDtos);
    }

    @Transactional
    public FindAllGoodsListDTO findAllGoods(
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GoodEntity> pageResult = goodRepository.findAll(pageable);
        return FindAllGoodsListDTO.from(pageResult);
    }

    @Transactional
    public FindAllStoresListDTO findAllStores() {
        List<StoreEntity> storeEntities = storeRepository.findAll();

        List<FindAllStoresDTO> storeDtos = storeEntities.stream()
                .map(FindAllStoresDTO::from)
                .toList();

        return new FindAllStoresListDTO(storeDtos);
    }

    @Transactional
    public FindAllRegionCodesListDTO findAllRegionCodes() {
        List<RegionCodeEntity> regionCodesEntities = regionCodeRepository.findAll();

        List<FindAllRegionCodesDTO> regionCodesDtos = regionCodesEntities.stream()
                .map(FindAllRegionCodesDTO::from)
                .toList();

        return new FindAllRegionCodesListDTO(regionCodesDtos);
    }

    @Transactional
    public FindAllPricesListDTO findAllPrices() {
        List<PriceEntity> pricesEntities = priceRepository.findAll();

        List<FindAllPricesDTO> priceDtos = pricesEntities.stream()
                .map(FindAllPricesDTO::from)
                .toList();

        return new FindAllPricesListDTO(priceDtos);
    }

    /**
     * 공공 API로부터 상품 데이터를 조회하고 저장한다
     *
     * @return 공공 API로부터 수신한 원본 XML 문자열
     */
    public SseEmitter fetchGoodsApi() {
        SseEmitter emitter = new SseEmitter(0L);

        new Thread(() -> {
            try {
                Consumer<String> log = msg -> safeSend(emitter, msg);

                log.accept("상품 데이터 추가 시작");

                String xml = publicApiService.fetchString(
                        "/getProductInfoSvc.do",
                        "상품"
                );
                log.accept("공공데이터 응답 수신 완료 (xml length = " + xml.length() + ")");

                GoodApiResponseDTO parsed = publicApiService.parseGoodsResponse(xml);
                int count = parsed.result().item() == null ? 0 : parsed.result().item().size();
                log.accept("XML 파싱 완료 (items = " + count + ")\n\n");

                int saved = publicApiService.saveGoods(parsed, log);
                log.accept("\nDB 반영 완료 (applied = " + saved + ")");
                log.accept("상품 데이터 추가 완료");

                emitter.complete();
            } catch (Exception e) {
                safeSend(emitter, "오류: " + e.getMessage());
                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    public SseEmitter fetchStoresApi() {
        SseEmitter emitter = new SseEmitter(0L);

        new Thread(() -> {
            try {
                Consumer<String> log = msg -> safeSend(emitter, msg);

                log.accept("매장 데이터 추가 시작");

                String xml = publicApiService.fetchString(
                        "/getStoreInfoSvc.do",
                        "매장"
                );
                log.accept("공공데이터 응답 수신 완료 (xml length = " + xml.length() + ")");

                StoreApiResponseDTO parsed = publicApiService.parseStoresResponse(xml);
                int count = parsed.result().item() == null ? 0 : parsed.result().item().size();
                log.accept("XML 파싱 완료 (items = " + count + ")\n\n");

                int saved = publicApiService.saveStores(parsed, log);
                log.accept("\nDB 반영 완료 (applied = " + saved + ")");
                log.accept("매장 데이터 추가 완료");

                emitter.complete();
            } catch (Exception e) {
                safeSend(emitter, "오류: " + e.getMessage());
                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    public SseEmitter fetchRegionCodesApi() {
        SseEmitter emitter = new SseEmitter(0L);

        new Thread(() -> {
            try {
                Consumer<String> log = msg -> safeSend(emitter, msg);

                log.accept("지역코드 데이터 추가 시작");

                String xml = publicApiService.fetchString(
                        "/getStandardInfoSvc.do",
                        "지역코드",
                        Map.of("classCode", "AR")
                );
                log.accept("공공데이터 응답 수신 완료 (xml length = " + xml.length() + ")");

                RegionCodeApiResponseDTO parsed = publicApiService.parseRegionCodesResponse(xml);
                int count = parsed.result().item() == null ? 0 : parsed.result().item().size();
                log.accept("XML 파싱 완료 (items = " + count + ")\n\n");

                int saved = publicApiService.saveRegionCodes(parsed, log);
                log.accept("\nDB 반영 완료 (applied = " + saved + ")");
                log.accept("지역코드 데이터 추가 완료");

                emitter.complete();
            } catch (Exception e) {
                safeSend(emitter, "오류: " + e.getMessage());
                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    public SseEmitter fetchPricesApi(String inspectDay) {
        SseEmitter emitter = new SseEmitter(0L);

        new Thread(() -> {
            try {
                Consumer<String> log = msg -> safeSend(emitter, msg);

                if (priceRepository.existsByInspectDay(inspectDay)) {
                    log.accept("이미 해당 일자의 가격 데이터가 존재합니다 (inspectDay = " + inspectDay + ")");
                    log.accept("작업을 종료합니다");
                    emitter.complete();
                    return;
                }

                log.accept("가격 데이터 추가 시작 (inspectDay = " + inspectDay + ")");

                List<Long> storeIds = storeRepository.findAllStoreIds();
                log.accept("대상 매장 수 = " + storeIds.size());

                for (int i = 0; i < storeIds.size(); i++) {
                    Long storeId = storeIds.get(i);

                    String storeName = storeRepository.findStoreNameByStoreId(storeId);

                    log.accept("\n[" + (i + 1) + "/" + storeIds.size() + "]\nstoreId = " + storeId +
                            " (" + storeName + ")");

                    if (!storeRepository.existsByStoreId(storeId)) {
                        log.accept("DB에 해당 매장 없으므로 스킵\n");
                        continue;
                    }

                    try {
                        String xml = publicApiService.fetchString(
                                "/getProductPriceInfoSvc.do",
                                "가격",
                                Map.of("goodInspectDay", inspectDay,
                                        "entpId", storeId.toString())
                        );
                        log.accept("공공데이터 응답 수신 완료 (xml length = " + xml.length() + ")");

                        PriceApiResponseDTO parsed = publicApiService.parsePricesResponse(xml);

                        int count = parsed.result().item() == null ? 0 : parsed.result().item().size();
                        log.accept("XML 파싱 완료 (items = " + count + ")\n\n");

                        int saved = publicApiService.savePrices(parsed, log);

                        if (saved != 0) {
                            log.accept("\nDB 반영 완료 (applied = " + saved + ")");
                            log.accept("가격 데이터 추가 완료");
                        }
                    } catch (Exception e) {
                        log.accept("오류 (storeId = " + storeId + "): " + e.getMessage());
                    }
                }

                emitter.complete();
            } catch (Exception e) {
                safeSend(emitter, "오류: " + e.getMessage());
                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    private void safeSend(SseEmitter emitter, String msg) {
        try {
            emitter.send(SseEmitter.event().name("log").data(msg));
        } catch (IOException ignored) {
            emitter.complete();
        }
    }
}
