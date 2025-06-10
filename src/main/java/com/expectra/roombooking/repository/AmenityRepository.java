package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    // Find all amenities for a specific room
    @Query("SELECT a FROM Amenity a JOIN a.rooms r WHERE r.roomId = :roomId")
    List<Amenity> findAllAmenitiesByRoomId(Long roomId);

    // Find all amenities for a specific hotel
    @Query("SELECT a FROM Amenity a JOIN a.hotels h WHERE h.hotelId = :hotelId")
    List<Amenity> findAllAmenitiesByHotelId(Long hotelId);

}
