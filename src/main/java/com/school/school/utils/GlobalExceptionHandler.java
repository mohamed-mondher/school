package com.school.school.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(java.lang.Exception.class)
    public ResponseEntity<String> handleUserNotFoundException(Exception ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(ex.getMessage());
    }

}
