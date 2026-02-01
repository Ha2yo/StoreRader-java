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

import com.storerader.server.domain.admin.dto.FindAllUsersListResponseDTO;
import com.storerader.server.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/select/users")
    public ResponseEntity<FindAllUsersListResponseDTO> getAllUsers() {
        return ResponseEntity.ok(adminService.findAllUsers());
    }

    @GetMapping("/get/public-data/goods")
    public String fetchGoodsApi() {
        return adminService.fetchGoodApi();
    }
}
