package com.example.hotelmanagement.service;

import com.example.hotelmanagement.dto.RoomRequest;
import com.example.hotelmanagement.dto.RoomResponse;
import com.example.hotelmanagement.entity.Room;
import com.example.hotelmanagement.exception.DuplicateRoomException;
import com.example.hotelmanagement.exception.RoomNotAvailableException;
import com.example.hotelmanagement.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    public List<RoomResponse> getAllRooms(){
        return roomRepository.findAll().stream().map(RoomResponse::from).toList();
    }

    public RoomResponse getRoomByRoomNumber(int roomNumber){
        Room room  = findRoomOrThrow(roomNumber);

        return RoomResponse.from(room);
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request){

        if(roomRepository.existsByRoomNumber(request.roomNumber())){
            throw new DuplicateRoomException(request.roomNumber());
        }

        Room room = new Room(request.roomNumber(),request.roomType(),request.price(),request.available());
        Room save = roomRepository.save(room);
        return RoomResponse.from(save);
    }

    @Transactional
    public void deleteRoom(int roomNumber){
        Room room = findRoomOrThrow(roomNumber);

        roomRepository.delete(room);
    }

    private Room findRoomOrThrow(int roomNumber){
        return roomRepository.findByRoomNumber(roomNumber).orElseThrow(()-> new RoomNotAvailableException(roomNumber));
    }
}
