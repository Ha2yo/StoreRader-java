package com.storerader.server.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<Map<String, String>> CustomException(CustomException e, HttpServletRequest request){

        log.error("Advice 내 customExceptionHandler() 호출, {}, {}", request.getRequestURI(), e.getMessage());

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("error_type", e.getHttpStatusType());
        responseMap.put("code", String.valueOf(e.getHttpStatusCode()));
        responseMap.put("message", e.getMessage());

        return ResponseEntity.status(e.getHttpStatus()).body(responseMap);
    }
}
