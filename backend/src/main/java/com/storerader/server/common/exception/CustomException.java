package com.storerader.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ExceptionClass exceptionClass;

    public CustomException(ExceptionClass exceptionClass) {
        super(exceptionClass.getMessage());
        this.exceptionClass = exceptionClass;
    }

    public HttpStatus getHttpStatus() {
        return exceptionClass.getStatus();
    }
}
