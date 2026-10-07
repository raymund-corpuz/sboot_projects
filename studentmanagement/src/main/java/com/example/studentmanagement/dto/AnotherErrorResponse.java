package com.example.studentmanagement.dto;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Map;

public record AnotherErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String,String> fieldErrors) {

    public static AnotherErrorResponse of(int status, String error, String message, Map<String, String> fieldErrors){
        return new AnotherErrorResponse(LocalDateTime.now(), status, error, message, null);
    }

    public static AnotherErrorResponse ofValidation(int status, String error, String message, Map<String, String> fieldErrors){
        return new AnotherErrorResponse(LocalDateTime.now(), status, error, message, fieldErrors);
    }
}
