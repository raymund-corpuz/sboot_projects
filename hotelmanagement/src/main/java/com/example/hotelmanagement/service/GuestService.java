package com.example.hotelmanagement.service;

import com.example.hotelmanagement.dto.GuestRequest;
import com.example.hotelmanagement.dto.GuestResponse;
import com.example.hotelmanagement.entity.Guest;
import com.example.hotelmanagement.exception.DuplicateEmailException;
import com.example.hotelmanagement.exception.GuestNotFoundException;
import com.example.hotelmanagement.repository.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository){
        this.guestRepository = guestRepository;
    }

    public List<GuestResponse> getAllGuests(){
        return guestRepository.findAll().stream().map(GuestResponse::from).toList();
    }

    public GuestResponse getGuest(Long id){
        Guest guest = findGuestOrThrow(id);

        return GuestResponse.from(guest);
    }

    public GuestResponse createGuest(GuestRequest request){

        if(guestRepository.existsByEmail(request.email())){
            throw  new DuplicateEmailException(request.email());
        }

        Guest guest =  new Guest(request.firstName(), request.lastName(), request.email(), request.phone(), request.numberOfBookings());
        Guest save = guestRepository.save(guest);

        return GuestResponse.from(save);
    }

    @Transactional
    public GuestResponse updateGuest(Long id, GuestRequest request){
        Guest guest = findGuestOrThrow(id);

        boolean emailChanged = !request.email().equals(guest.getEmail());

        if(emailChanged && guestRepository.existsByEmail(guest.getEmail())){
            throw new DuplicateEmailException(request.email());
        }

        guest.setFirstName(request.firstName());
        guest.setLastName(request.lastName());
        guest.setEmail(request.email());
        guest.setPhone(request.phone());
        guest.setNumberOfBookings(request.numberOfBookings());

        return GuestResponse.from(guest);

    }

    @Transactional
    public void deleteGuest(Long id){
        Guest guest = findGuestOrThrow(id);
        guestRepository.delete(guest);
    }

    private Guest findGuestOrThrow(Long id){
        return guestRepository.findById(id).orElseThrow(()-> new GuestNotFoundException(id));
    }


}
