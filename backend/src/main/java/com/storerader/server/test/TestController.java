package com.storerader.server.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "테스트 API", description = "Swagger 테스트용 API")
@RestController
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    @Operation(
            summary = "백엔드 응답 테스트",
            description = "간단한 문자열을 전송함으로써 서버의 상태를 점검합니다."
    )
    @GetMapping("/")
    public String index() {
        return "index";
    }
}