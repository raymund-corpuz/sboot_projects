package com.example.hotelmanagement.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(Long id) {

        super("Booking ID not found: " + id);
    }
}
