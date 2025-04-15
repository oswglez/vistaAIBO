package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.AmenityDTO;
import com.expectra.roombooking.model.Amenity;
import java.util.List;
import java.util.Optional;

public interface AmenityService {
    // Operaciones básicas CRUD
    Amenity createAmenity(Long hotelId, Amenity amenity);
    Optional<Amenity> getAmenityById(Long id);
    List<Amenity> getAllAmenities();
    Amenity updateAmenity(Long id, Amenity amenityDetails);
    void deleteAmenity(Long id);

    // Operaciones específicas para relaciones
    List<Amenity> getAmenitiesByRoomId(Long roomId);
    List<Amenity> getAmenitiesByHotelId(Long hotelId);
    Amenity addAmenityToRoom(Long roomId, Long amenityId);
    Amenity addAmenityToHotel(Long hotelId, Long amenityId);
    void removeAmenityFromRoom(Long roomId, Long amenityId);
    void removeAmenityFromHotel(Long hotelId, Long amenityId);
}