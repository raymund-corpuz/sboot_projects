package com.example.studentmanagement.dto;

public record TokenResponse (String accessToken, String tokenType, long expiresIn, UserResponse user){
}
