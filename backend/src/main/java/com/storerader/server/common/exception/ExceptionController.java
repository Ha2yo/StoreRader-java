package com.storerader.server.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController{

    @GetMapping("/exception")
    public void throwCustomException() throws CustomException {
        throw new CustomException(
                ExceptionClass.INVALID_REQUEST,
                HttpStatus.BAD_REQUEST,
                "Custom Exception Controller Test"
        );
    }
}
