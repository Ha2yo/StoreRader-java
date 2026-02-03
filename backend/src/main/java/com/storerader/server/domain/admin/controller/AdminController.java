/*
 * File: domain/admin/controller/AdminController.java
 * Description:
 *     관리자(admin) 도메인의 컨트롤러 계층으로,
 *     관리자 전용 기능에 대한 HTTP 요청을 처리한다
 *
 * Responsibilities:
 *      1) getAllUsers()
 *          - 전체 유저 리스트 조회
 *
 *      2) fetchGoodsApi()
 *          - 공공데이터포털로부터 상품(goods) 데이터 조회
 */

package com.storerader.server.domain.admin.controller;

import com.storerader.server.domain.admin.dto.select.goods.FindAllGoodsListResponseDTO;
import com.storerader.server.domain.admin.dto.select.users.FindAllUsersListResponseDTO;
import com.storerader.server.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/select/users")
    public ResponseEntity<FindAllUsersListResponseDTO> getAllUsers() {
        return ResponseEntity.ok(adminService.findAllUsers());
    }

    @GetMapping("/select/goods")
    public ResponseEntity<FindAllGoodsListResponseDTO> getAllGoods() {
        return ResponseEntity.ok(adminService.findAllGoods());
    }

    @GetMapping(value = "/get/public-data/goods", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter fetchGoodsApi() {
        return adminService.fetchGoodApi();
    }
}
