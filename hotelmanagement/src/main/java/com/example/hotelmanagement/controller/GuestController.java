package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.dto.GuestRequest;
import com.example.hotelmanagement.dto.GuestResponse;
import com.example.hotelmanagement.entity.Guest;
import com.example.hotelmanagement.exception.GuestNotFoundException;
import com.example.hotelmanagement.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/guests")

public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping
    public List<GuestResponse> getAllGuests(@RequestBody GuestRequest request){
        return guestService.getAllGuests();
    }

    @GetMapping("/{id}")
    public GuestResponse getGuestById(@Valid @PathVariable Long guestId){
        return guestService.getGuest(guestId);
    }

    @PostMapping
    public ResponseEntity<GuestResponse> createGuest(@RequestBody GuestRequest request){

        GuestResponse guest = guestService.createGuest(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(guest.id()).toUri();

        return ResponseEntity.created(location).body(guest);
    }

    @PutMapping("/{id}")
    public GuestResponse updateGuest(@PathVariable Long guestId, @RequestBody GuestRequest request){
        return guestService.updateGuest(guestId,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGuest(Long guestId){
        guestService.deleteGuest(guestId);
    }
}
