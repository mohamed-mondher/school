package com.school.school.utils;

import org.springframework.http.HttpStatus;

public class Exception extends RuntimeException {


    private final HttpStatus status;

    public Exception(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
