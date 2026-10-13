package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByGuestId(Long guestId);

    Optional<Booking> findByBookingId(Long bookingId);

    Optional<Booking> findByRoomId(Long roomId);

    void deleteByGuestId(Long guestId);

}
