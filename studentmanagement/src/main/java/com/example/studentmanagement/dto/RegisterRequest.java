package com.example.studentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest (

        @NotBlank(message = "Username is required")
        @Size(min = 3,  max = 50 , message = "Username must be 3 to 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username may contain only letters, digits, dots, underscores and hyphens")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min= 12, max = 72, message = "Password must be 12 to 72 characters")
        String password
) {
}
