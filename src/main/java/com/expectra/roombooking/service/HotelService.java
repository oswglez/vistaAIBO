package com.expectra.roombooking.service;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    public Hotel updateHotel(Long id, Hotel hotelDetails) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        hotel.setName(hotelDetails.getName());
        hotel.setCategory(hotelDetails.getCategory());
        hotel.setRoomQty(hotelDetails.getRoomQty());
        hotel.setStories(hotelDetails.getStories());

        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        hotelRepository.delete(hotel);
    }
}
