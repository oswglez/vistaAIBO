package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;

import java.util.List;
import java.util.Optional;

public interface AddressService {
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

}