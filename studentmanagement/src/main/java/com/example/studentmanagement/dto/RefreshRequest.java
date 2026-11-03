package com.example.studentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshRequest(

        @NotBlank(message = "Refresh token is required")
        @Size(max = 200, message = "Refresh token is too long")
        String refreshToken
){

};