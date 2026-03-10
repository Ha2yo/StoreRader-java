package com.storerader.server.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorRes> CustomException(CustomException e, HttpServletRequest request){

        ExceptionClass exceptionClass = e.getExceptionClass();

        log.error("Advice 내 customExceptionHandler() 호출, {}, {}", request.getRequestURI(), e.getMessage());

        ErrorRes response = new ErrorRes(
                exceptionClass.getStatus().value(),
                exceptionClass.getCode(),
                exceptionClass.getMessage()
        );

        return ResponseEntity
                .status(exceptionClass.getStatus())
                        .body(response);
    }
}
