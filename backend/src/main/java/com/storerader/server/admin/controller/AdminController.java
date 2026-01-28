package com.storerader.server.admin.controller;

import com.storerader.server.admin.dto.FindAllUsersListResponse;
import com.storerader.server.admin.service.AdminService;
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

    @GetMapping("/users")
    public ResponseEntity<FindAllUsersListResponse> getAllUsers() {
        return ResponseEntity.ok(adminService.findAllUsers());
    }
}
