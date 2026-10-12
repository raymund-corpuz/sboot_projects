package com.example.hotelmanagement.dto;

import com.example.hotelmanagement.entity.Guest;

public record GuestResponse (Long id, String firstName, String lastName, String email, String phone, int numberOfBookings){

    public static GuestResponse from(Guest guest){
        return new GuestResponse(guest.getId(), guest.getFirstName(), guest.getLastName(), guest.getEmail(), guest.getPhone(), guest.getNumberOfBookings());
    }
}
