package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>{

    Optional<Room> findById(Long id);

    boolean existsByRoomNumber(Integer roomNumber);

}
