package com.example.hotelmanagement.service;

import com.example.hotelmanagement.dto.GuestRequest;
import com.example.hotelmanagement.dto.GuestResponse;
import com.example.hotelmanagement.entity.Guest;
import com.example.hotelmanagement.exception.GuestNotFoundException;
import com.example.hotelmanagement.repository.GuestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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



    private Guest findGuestOrThrow(Long id){
        return guestRepository.findById(id).orElseThrow(()-> new GuestNotFoundException(id));
    }


}
