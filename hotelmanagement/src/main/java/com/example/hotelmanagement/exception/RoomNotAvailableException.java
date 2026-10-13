package com.example.hotelmanagement.exception;

public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException(Integer roomNumber) {

        super("Room number not found :" + roomNumber);
    }
}
