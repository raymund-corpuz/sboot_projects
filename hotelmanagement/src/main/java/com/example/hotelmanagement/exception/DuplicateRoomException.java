package com.example.hotelmanagement.exception;

public class DuplicateRoomException extends RuntimeException {
    public DuplicateRoomException(int roomNumber) {

        super("Room number already exists: " + roomNumber);
    }
}
