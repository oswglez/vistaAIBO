package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public interface  AmenityRepository extends JpaRepository<Amenity, Long> {

    // Método personalizado para encontrar todas las medias de una habitación específica
    @Query("SELECT a FROM Amenity a JOIN a.rooms r WHERE r.roomId = :roomId")
    List<Amenity> findAllAmenitiesByRoomId(Long roomId);

    // Método personalizado para encontrar todas las medias de un hotel específico
    @Query("SELECT a FROM Amenity a JOIN a.hotels h WHERE h.hotelId = :hotelId")
    List<Amenity> findAllAmenitiesByHotelId(Long hotelId);

}
