package com.storerader.server.common.exception;

public record ErrorRes (
        int status,
        String code,
        String message
) {}
