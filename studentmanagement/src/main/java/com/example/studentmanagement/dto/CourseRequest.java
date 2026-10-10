package com.example.studentmanagement.dto;

import jakarta.validation.constraints.*;

public record CourseRequest(

        @NotBlank(message = "Code is required")
        @Size(max = 20, message = "This code must be at most 20 characters")
        String code,

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "This title must be at most 100 characters")
        String title,

        @NotNull(message="Credits is required")
        @Min(value = 1, message = "Credits must be at least 1")
        @Max(value =10, message = "Credits must be at most 10")
        int credits

) {
}
