package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.AmenityDTO;
import com.expectra.roombooking.model.Amenity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AmenityService {
    // Basic CRUD operations
    Amenity createAmenity(Amenity amenity);
    Optional<Amenity> getAmenityById(Long id);
    Page<Amenity> getAllAmenities(Pageable pageable);
    Amenity updateAmenity(Long id, Amenity amenityDetails);
    void deleteAmenity(Long id);

    // Relationship operations
    List<Amenity> getAmenitiesByRoomId(Long roomId);
    List<Amenity> getAmenitiesByHotelId(Long hotelId);
    Amenity addAmenityToRoom(Long roomId, Long amenityId);
    Amenity addAmenityToHotel(Long hotelId, Long amenityId);
    void removeAmenityFromRoom(Long roomId, Long amenityId);
    void removeAmenityFromHotel(Long hotelId, Long amenityId);
}