package com.example.hotelmanagement.exception;

public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(Long id) {
        super("Guest not found with an id: " + id);
    }
}
