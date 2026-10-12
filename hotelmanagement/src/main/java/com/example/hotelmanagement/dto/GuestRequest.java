package com.example.hotelmanagement.dto;

import jakarta.validation.constraints.*;

public record GuestRequest(

        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must be at most 50 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must be at most 50 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email,

        @NotBlank(message = "Phone is required")
        @Size(max=20, message = "Phone must be at most 50 characters")
        String phone,

        @NotNull(message = "Number of bookings is required")
        @Min(value = 1, message = "Number of bookings must be at least 1")
        @Max(value = 10, message = "Number of Bookings must be at most 10")
        int numberOfBookings) {
}
