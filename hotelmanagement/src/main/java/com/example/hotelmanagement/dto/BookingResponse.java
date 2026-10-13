package com.example.hotelmanagement.dto;

import com.example.hotelmanagement.entity.Booking;
import com.example.hotelmanagement.entity.enums.BookingStatus;

import java.time.LocalDate;

public record BookingResponse(Long id, Long guestId, Long roomId, LocalDate checkIn, LocalDate checkOut, BookingStatus status) {

    public static BookingResponse from(Booking booking){
        return new BookingResponse(booking.getBookingId(), booking.getGuest().getId(), booking.getRoom().getId(),booking.getCheckIn(),booking.getCheckOut(),booking.getStatus());
    }
}
