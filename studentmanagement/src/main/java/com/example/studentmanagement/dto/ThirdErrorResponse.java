package com.example.studentmanagement.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ThirdErrorResponse(LocalDateTime timestamp, int status, String error, String message, Map<String, String>fieldErrors) {

    public static ThirdErrorResponse of(int status, String error, String message){
        return  new ThirdErrorResponse(LocalDateTime.now(), status,error, message,null);
    }

    public static ThirdErrorResponse ofValidation(int status,String error, String message, Map<String, String> fieldErrors){
        return new ThirdErrorResponse(LocalDateTime.now(), status,error,message,fieldErrors);
    }

}
