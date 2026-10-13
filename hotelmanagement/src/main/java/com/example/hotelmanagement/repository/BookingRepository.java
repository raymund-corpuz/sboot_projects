package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.entity.Booking;
import com.example.hotelmanagement.entity.Guest;
import com.example.hotelmanagement.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByGuestId(Long guestId);

    Optional<Booking> findByBookingId(Long bookingId);

    Optional<Booking> findByRoomId(Long roomId);

    void deleteByGuestId(Long guestId);

    @Query("select e.guest from Booking e where e.roomNumber = :roomNumber")
    List<Guest> findGuestsByRoomNumber(@Param("roomNumber") Integer roomNumber);

    @Query("select e.roomNumber from Booking e where e.guestId = : guestId")
    List<Room> findRoomByGuestId(@Param("guestId") Long guestId);


}
