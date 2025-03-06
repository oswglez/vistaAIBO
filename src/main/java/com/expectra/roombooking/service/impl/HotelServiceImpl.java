package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.model.*;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> findHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    @Override
    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public void deleteHotelById(Long hotelId) {
        hotelRepository.deleteById(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findHotelsByName(String hotelName) {
        return hotelRepository.findByHotelName(hotelName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Amenity> findHotelAmenities(Long hotelId) {
        return hotelRepository.findAllAmenitiesByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Media> findHotelMedias(Long hotelId) {
        return hotelRepository.findAllMediasByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contact> findAllContactsByHotelId(Long hotelId) {
        return hotelRepository.findAllContactsByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findAllAddressesByHotelId(Long hotelId) {
        return hotelRepository.findAllAddressesByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> findHotelRooms(Long hotelId) {
        return hotelRepository.findAllRoomsByHotelId(hotelId);
    }
}