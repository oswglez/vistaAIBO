package com.expectra.roombooking.service;

import com.expectra.roombooking.model.*;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    // Operaciones CRUD básicas
    List<Hotel> findAllHotels();
    Optional<Hotel> findHotelById(Long hotelId);
    Hotel saveHotel(Hotel hotel);
    void deleteHotel(Hotel hotel);
    void deleteHotelById(Long hotelId);

    // Operaciones específicas
    List<Hotel> findHotelsByName(String hotelName);
    List<Amenity> findHotelAmenities(Long hotelId);
    List<Media> findHotelMedias(Long hotelId);
    List<Room> findHotelRooms(Long hotelId);
    List<Contact> findAllContactsByHotelId(Long hotelId);



}