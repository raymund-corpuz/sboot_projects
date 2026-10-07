package com.example.studentmanagement.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record AnotherStudentRequest (

        @NotBlank(message = "First name is required")
        @Size(max=50, message = "First name must be at most 50 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max=50, message ="Last name must be at most 50 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be at past")
        LocalDate dateOfBirth

){
}
