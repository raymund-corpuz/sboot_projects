package com.example.hotelmanagement.dto;

import com.example.hotelmanagement.entity.enums.RoomType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record RoomRequest(

        @NotNull(message = "Room number is required")
        @PositiveOrZero(message = "Room number cannot be negative")
        int roomNumber,

        @NotNull(message = "Room type is required")
       @Enumerated(EnumType.STRING)
        RoomType roomType,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price must be 0 or greater")
        BigDecimal price,

        @NotBlank(message = "Available status is required")
        boolean available) {
}
