package com.storerader.server.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "테스트 API", description = "서버 상태 점검용 API")
@RestController
@RequestMapping("/test")
public class TestController {
    @Operation(
            summary = "서버 상태 확인",
            description = "서버가 정상적으로 동작하는지 확인하기 위한 헬스체크용 엔드포인트입니다."
    )
    @GetMapping("/")
    public String index() {
        return "success";
    }
}