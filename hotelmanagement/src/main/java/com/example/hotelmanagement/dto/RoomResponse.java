package com.example.hotelmanagement.dto;

import com.example.hotelmanagement.entity.Room;
import com.example.hotelmanagement.entity.enums.RoomType;

import java.math.BigDecimal;

public record RoomResponse(Long id, int roomNumber, RoomType roomType, BigDecimal price, boolean available) {

    public static RoomResponse from(Room room){
        return new RoomResponse(room.getId(), room.getRoomNumber(), room.getRoomType(), room.getPrice(), room.isAvailable());
    }
}
