package com.example.hotelmanagement.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record RoomRequest(

        @NotNull(message = "Room number is required")
        @PositiveOrZero(message = "Room number cannot be negative")
        int roomNumber,

        @NotBlank(message = "Room type is required")
        @Size(max = 50, message = "Room type must be at most 50 characters")
        String roomType,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be 0 or greater")
        BigDecimal price,

        @NotBlank(message = "Available status is required")
        boolean available) {
}
