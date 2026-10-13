package com.example.hotelmanagement.service;

import com.example.hotelmanagement.dto.BookingResponse;
import com.example.hotelmanagement.dto.GuestResponse;
import com.example.hotelmanagement.dto.RoomResponse;
import com.example.hotelmanagement.entity.Booking;
import com.example.hotelmanagement.entity.Guest;
import com.example.hotelmanagement.entity.Room;
import com.example.hotelmanagement.exception.BookingNotFoundException;
import com.example.hotelmanagement.exception.GuestNotFoundException;
import com.example.hotelmanagement.exception.RoomNotAvailableException;
import com.example.hotelmanagement.repository.BookingRepository;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, GuestRepository guestRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public BookingResponse createBooking(Long guestId, Integer roomNumber, LocalDate checkIn, LocalDate checkOut){

        Guest guest = guestRepository.findById(guestId).orElseThrow(()-> new GuestNotFoundException(guestId));

        Room room = roomRepository.findByRoomNumber(roomNumber).orElseThrow(()-> new RoomNotAvailableException(roomNumber));

        room.setAvailable(false);
        Booking booking = new Booking(guest, room,checkIn,checkOut );
        Booking save = bookingRepository.save(booking);

        return BookingResponse.from(save);
   }

   @Transactional
    public void cancelBooking(Long id){
        Booking booking = findBookingIdOrThrow(id);

        booking.getRoom().setAvailable(true);
        bookingRepository.delete(booking);
   }

   public BookingResponse getBookingById(Long id){
        Booking booking = findBookingIdOrThrow(id);
        return BookingResponse.from(booking);
   }

   public Booking findBookingIdOrThrow(Long id){
        return bookingRepository.findByBookingId(id).orElseThrow(()->new BookingNotFoundException(id));
   }

   public List<GuestResponse> getGuestByRoom(Integer roomNumber){

      if(!roomRepository.existsByRoomNumber(roomNumber)){
          throw new RoomNotAvailableException(roomNumber);
      }
      return bookingRepository.findGuestsByRoomNumber(roomNumber).stream().map(GuestResponse::from).toList();
   }

   public List<RoomResponse> getRoomByGuest(Long guestId){

        if(!guestRepository.existsById(guestId)){
            throw new GuestNotFoundException(guestId);
        }

        return bookingRepository.findRoomByGuestId(guestId).stream().map(RoomResponse::from).toList();
   }

}
