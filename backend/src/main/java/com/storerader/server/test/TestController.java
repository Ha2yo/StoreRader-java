package com.storerader.server.test;

import com.storerader.server.common.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class TestController {

    private final TestService testService;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/test")
    public List<UsersEntity> getAllUsers() {
        return testService.findAll();
    }
}

//@RequestMapping("/auth")
//public class AuthController {
//
//    private final AuthService authService;
//
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    @PostMapping("/google")
//    public String googleLogin(@RequestBody GoogleLoginRequest req) {
//        authService.insertUserFromIdTokenForTest(req.getIdToken());
//        return "ok";
//    }
//}