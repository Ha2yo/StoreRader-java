package com.storerader.server.common.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController{

    @GetMapping("/exception")
    public void throwCustomException() {
        throw new CustomException(ExceptionClass.INVALID_REQUEST);
    }
}
