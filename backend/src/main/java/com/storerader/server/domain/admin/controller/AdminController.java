package com.storerader.server.admin.controller;

import com.storerader.server.admin.dto.FindAllUsersListResponse;
import com.storerader.server.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<FindAllUsersListResponse> getAllUsers(HttpServletRequest request) {
        return ResponseEntity.ok(adminService.findAllUsers());
    }

    @GetMapping("/get/public-data/goods")
    public String fetchGoodsApi() {
        return adminService.fetchGoodApi();
    }
}
