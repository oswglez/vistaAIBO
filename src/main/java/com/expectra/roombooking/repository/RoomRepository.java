package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findByHotelHotelId(Long hotelId, Pageable pageable);

    // Find all media for a specific room in a hotel
    @Query("SELECT m FROM Media m JOIN m.rooms r WHERE r.roomId = :roomId AND r.hotel.hotelId = :hotelId")
    List<Media> getAllMediasByHotelIdAndRoomId(@Param("hotelId") Long hotelId, @Param("roomId") Long roomId);

    // Find all media for a specific room type in a hotel
    @Query("SELECT m FROM Media m JOIN m.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomType = :roomType")
    List<Media> getAllMediasByHotelIdAndRoomType(@Param("hotelId") Long hotelId, @Param("roomType") String roomType);

    // Find all amenities for a specific room in a hotel
    @Query("SELECT a FROM Amenity a JOIN a.rooms r WHERE r.roomId = :roomId AND r.hotel.hotelId = :hotelId")
    List<Amenity> getAllAmenitiesByHotelIdAndRoomId(@Param("hotelId") Long hotelId, @Param("roomId") Long roomId);

    // Find all amenities for a specific room type in a hotel
    @Query("SELECT a FROM Amenity a JOIN a.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomType = :roomType")
    List<Amenity> getAllAmenitiesByHotelIdAndRoomType(Long hotelId, String roomType);
}
