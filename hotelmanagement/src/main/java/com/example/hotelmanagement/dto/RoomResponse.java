package com.example.hotelmanagement.dto;

import com.example.hotelmanagement.entity.Room;

import java.math.BigDecimal;

public record RoomResponse(int roomNumber, String roomType, BigDecimal price, boolean available) {

    public static RoomResponse from(Room room){
        return new RoomResponse(room.getRoomNumber(), room.getRoomType(), room.getPrice(), room.isAvailable());
    }
}
