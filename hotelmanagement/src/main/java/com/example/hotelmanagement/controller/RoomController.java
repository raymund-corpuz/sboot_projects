package com.example.hotelmanagement.controller;

import com.example.hotelmanagement.dto.RoomRequest;
import com.example.hotelmanagement.dto.RoomResponse;
import com.example.hotelmanagement.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<RoomResponse> getAllRooms(){
        return roomService.getAllRooms();
    }

    @GetMapping("/{roomNumber}")
    public RoomResponse getRoomNumber(@Valid @PathVariable Integer roomNumber){
        return roomService.getRoomByRoomNumber(roomNumber);
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request){
        RoomResponse room =  roomService.createRoom(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(room.id()).toUri();

        return ResponseEntity.created(location).body(room);
    }

    @DeleteMapping("/{roomNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@Valid @PathVariable Integer roomNumber){
        roomService.deleteRoom(roomNumber);
    }
}
