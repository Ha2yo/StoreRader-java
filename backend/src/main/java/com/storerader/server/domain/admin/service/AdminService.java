package com.storerader.server.domain.admin.service;

import com.storerader.server.common.entity.*;
import com.storerader.server.common.exception.CustomException;
import com.storerader.server.common.exception.ExceptionClass;
import com.storerader.server.common.repository.*;
import com.storerader.server.domain.admin.dto.add.RegionCode.RegionCodeApiResponse;
import com.storerader.server.domain.admin.dto.add.goods.GoodApiResponse;
import com.storerader.server.domain.admin.dto.add.prices.PriceApiResponse;
import com.storerader.server.domain.admin.dto.add.stores.StoreApiResponse;
import com.storerader.server.domain.admin.dto.select.goods.GoodList;
import com.storerader.server.domain.admin.dto.select.prices.PriceList;
import com.storerader.server.domain.admin.dto.select.regionCodes.RegionCodeList;
import com.storerader.server.domain.admin.dto.select.stores.StoreList;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 관리자 도메인 서비스
 * 관리자 페이지에서 사용하는 조회 및 공공데이터 적재 기능을 담당한다.
 */
@RequiredArgsConstructor
@Service
public class AdminService {

    private final GoodRepository goodRepository;
    private final StoreRepository storeRepository;
    private final PriceRepository priceRepository;
    private final RegionCodeRepository regionCodeRepository;
    private final PublicApiService publicApiService;

    /**
     * 상품 목록을 페이지 단위로 조회한다.
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지 크기
     * @param sortKey 정렬 기준 커럼
     * @param sortOrder 정렬 방향
     * @return 상품 목록 리스트
     */
    @Transactional
    public GoodList findAllGoods(
            int page,
            int size,
            String sortKey,
            String sortOrder
    ) {
        if (page < 0 || size <= 0) {
            throw new CustomException(ExceptionClass.INVALID_REQUEST);
        }

        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            throw new CustomException(ExceptionClass.INVALID_REQUEST);
        }

        Sort.Direction dir = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(dir, sortKey)
        );

        Page<GoodEntity> pageResult = goodRepository.findAll(pageable);
        return GoodList.from(pageResult);
    }

    /**
     * 매장 목록을 페이지 단위로 조회한다.
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지 크기
     * @param sortKey 정렬 기준 커럼
     * @param sortOrder 정렬 방향
     * @return 매장 목록 리스트
     */
    @Transactional
    public StoreList findAllStores(
            int page,
            int size,
            String sortKey,
            String sortOrder
    ) {
        if (page < 0 || size <= 0) {
            throw new CustomException(ExceptionClass.INVALID_REQUEST);
        }

        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            throw new CustomException(ExceptionClass.INVALID_REQUEST);
        }

        Sort.Direction dir = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(dir, sortKey)
        );

        Page<StoreEntity> pageResult = storeRepository.findAll(pageable);
        return StoreList.from(pageResult);
    }

    /**
     * 지역코드 목록을 페이지 단위로 조회한다.
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지 크기
     * @param sortKey 정렬 기준 커럼
     * @param sortOrder 정렬 방향
     * @return 지역코드 목록 리스트
     */
    @Transactional
    public RegionCodeList findAllRegionCodes(
            int page,
            int size,
            String sortKey,
            String sortOrder
    ) {
        if (page < 0 || size <= 0) {
            throw new CustomException(ExceptionClass.INVALID_REQUEST);
        }

        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            throw new CustomException(ExceptionClass.INVALID_REQUEST);
        }

        Sort.Direction dir = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(dir, sortKey)
        );

        Page<RegionCodeEntity> pageResult = regionCodeRepository.findAll(pageable);
        return RegionCodeList.from(pageResult);
    }

    /**
     * 가격 목록을 페이지 단위로 조회한다.
     *
     * @param page 조회할 페이지 번호
     * @param size 페이지 크기
     * @param sortKey 정렬 기준 커럼
     * @param sortOrder 정렬 방향
     * @return 가격 목록 리스트
     */
    @Transactional
    public PriceList findAllPrices(
            int page,
            int size,
            String sortKey,
            String sortOrder
    ) {
        if (page < 0 || size <= 0) {
            throw new CustomException(ExceptionClass.INVALID_REQUEST);
        }

        if (!"asc".equalsIgnoreCase(sortOrder) && !"desc".equalsIgnoreCase(sortOrder)) {
            throw new CustomException(ExceptionClass.INVALID_REQUEST);
        }

        Sort.Direction dir = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(dir, sortKey)
        );

        Page<PriceEntity> pageResult = priceRepository.findAll(pageable);
        return PriceList.from(pageResult);
    }

    /**
     * 공공 API에서 상품 데이터를 조회한 뒤 DB에 저장한다.
     * 처리 진행 상황은 SSE 로그로 클라이언트에 전달된다.
     *
     * @return 로그 전송을 위한 SSE emitter
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

                GoodApiResponse parsed = publicApiService.parseXML(xml, GoodApiResponse.class, "상품");
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

    /**
     * 공공 API에서 매장 데이터를 조회한 뒤 DB에 저장한다.
     * 처리 진행 상황은 SSE 로그로 클라이언트에 전달된다.
     *
     * @return 로그 전송을 위한 SSE emitter
     */
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

                StoreApiResponse parsed = publicApiService.parseXML(xml, StoreApiResponse.class, "매장");
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

    /**
     * 공공 API에서 지역코드 데이터를 조회한 뒤 DB에 저장한다.
     * 처리 진행 상황은 SSE 로그로 클라이언트에 전달된다.
     *
     * @return 로그 전송을 위한 SSE emitter
     */
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

                RegionCodeApiResponse parsed = publicApiService.parseXML(xml, RegionCodeApiResponse.class, "지역코드");
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

    /**
     * 공공 API에서 가격 데이터를 조회한 뒤 DB에 저장한다.
     * 처리 진행 상황은 SSE 로그로 클라이언트에 전달된다.
     *
     * @return 로그 전송을 위한 SSE emitter
     */
    public SseEmitter fetchPricesApi(String inspectDay) {
        // SSE 타임아웃 무제한 설정 (권장)
        SseEmitter emitter = new SseEmitter(-1L);

        new Thread(() -> {
            try {
                Consumer<String> log = msg -> safeSend(emitter, msg);

                log.accept("가격 데이터 추가 시작 (inspectDay = " + inspectDay + ")");

                List<Long> storeIds = storeRepository.findAllStoreIds();
                log.accept("대상 매장 수 = " + storeIds.size() + "\n");

                java.util.concurrent.atomic.AtomicInteger totalSaved = new java.util.concurrent.atomic.AtomicInteger(0);
                java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(5);

                try {
                    // 1. runAsync 대신 supplyAsync를 사용하여 비동기 작업을 생성합니다.
                    // 작업을 마친 뒤 출력할 "완성된 로그 문자열(String)"을 반환하도록 합니다.
                    List<java.util.concurrent.CompletableFuture<String>> futures = java.util.stream.IntStream.range(0, storeIds.size())
                            .mapToObj(i -> {
                                Long storeId = storeIds.get(i);
                                int currentIdx = i + 1;

                                return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                                    StringBuilder taskLog = new StringBuilder();
                                    String storeName = storeRepository.findStoreNameByStoreId(storeId);

                                    // 원하시는 포맷대로 조립: [1/590] \n storeId = 786 (...)
                                    taskLog.append(String.format("[%d/%d]\nstoreId = %d (%s)\n", currentIdx, storeIds.size(), storeId, storeName));

                                    if (!storeRepository.existsByStoreId(storeId)) {
                                        taskLog.append("DB에 해당 매장 없으므로 스킵\n");
                                        return taskLog.toString();
                                    }

                                    try {
                                        // API 호출
                                        String xml = publicApiService.fetchString(
                                                "/getProductPriceInfoSvc.do",
                                                "가격",
                                                Map.of("goodInspectDay", inspectDay,
                                                        "entpId", storeId.toString())
                                        );

                                        PriceApiResponse parsed = publicApiService.parseXML(xml, PriceApiResponse.class, "가격");
                                        int count = parsed.result().item() == null ? 0 : parsed.result().item().size();

                                        if (count == 0) {
                                            taskLog.append("데이터 없음\n");
                                        } else {
                                            // 내부에서 찍히는 로그는 무시하도록 빈 Consumer 전달
                                            int saved = publicApiService.savePrices(parsed, msg -> {});

                                            if (saved > 0) {
                                                totalSaved.addAndGet(saved);
                                                taskLog.append("DB 반영: ").append(saved).append("건\n");
                                            } else {
                                                taskLog.append("전체 중복 (반영 0건)\n");
                                            }
                                        }
                                    } catch (Exception e) {
                                        taskLog.append("오류 발생: ").append(e.getMessage()).append("\n");
                                    }

                                    return taskLog.toString();
                                }, executor);
                            }).toList();

                    // 2. 여기서 순서를 보장합니다!
                    // 작업 자체는 백그라운드에서 5개씩 병렬로 미친 듯이 돌고 있지만,
                    // 화면에 던져주는 로그는 무조건 1번 작업부터 순서대로 기다렸다가 조립해서 던집니다.
                    for (java.util.concurrent.CompletableFuture<String> future : futures) {
                        log.accept(future.join());
                    }

                } finally {
                    executor.shutdown();
                }

                log.accept("\n가격 데이터 추가 완료 (총 " + totalSaved.get() + "개 반영)");

                emitter.complete();
            } catch (Exception e) {
                safeSend(emitter, "오류: " + e.getMessage());
                emitter.completeWithError(e);
            }

        }).start();

        return emitter;
    }

    /**
     * SSE emitter로 로그 메시지를 전송한다.
     *
     * @param emitter SSE 이벤트를 전송할 SseEmiter 객체
     * @param msg 클라이언트로 전달할 로그 메시지
     */
    private void safeSend(SseEmitter emitter, String msg) {
        try {
            emitter.send(SseEmitter.event().name("log").data(msg));
        } catch (Exception ignored) {
            try {
                emitter.complete();
            } catch (Exception e) {
            }
        }
    }
}