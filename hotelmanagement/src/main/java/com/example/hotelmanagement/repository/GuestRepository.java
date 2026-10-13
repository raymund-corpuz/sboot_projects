package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest,Long> {

    boolean existsByEmail(String email);

    @Override
    boolean existsById(Long guestId);
}
