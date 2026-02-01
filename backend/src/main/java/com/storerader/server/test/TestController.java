package com.storerader.server.test;

import com.storerader.server.common.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class TestController {

    private final TestService testService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/test")
    public List<UserEntity> getAllUsers() {
        return testService.findAll();
    }
}