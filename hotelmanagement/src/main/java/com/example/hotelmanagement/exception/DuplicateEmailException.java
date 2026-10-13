package com.example.hotelmanagement.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Email :'" + email +"' already exists.");
    }
}
