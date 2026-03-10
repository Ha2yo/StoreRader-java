package com.storerader.server.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<Map<String, String>> CustomException(CustomException e, HttpServletRequest request){

        log.error("Advice 내 customExceptionHandler() 호출, {}, {}", request.getRequestURI(), e.getMessage());

        ExceptionClass exceptionClass = e.getExceptionClass();

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("error_type", exceptionClass.getStatus().getReasonPhrase());
        responseMap.put("code", exceptionClass.getCode());
        responseMap.put("message", exceptionClass.getCode() + ", " + exceptionClass.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(responseMap);
    }
}
