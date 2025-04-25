package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Metodo personalizado para encontrar todas las medias de una habitacion específica
//    @Query("SELECT m FROM Media m JOIN m.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomId = :roomId")
    @Query("SELECT m FROM Media m JOIN m.rooms r WHERE r.roomId = :roomId AND r.hotel.hotelId = :hotelId")
    List<Media> getAllMediasByHotelIdAndRoomId(@Param("hotelId") Long hotelId, @Param("roomId") Long roomId);

    // Metodo personalizado para encontrar todas las medias de un tipo de habitacion específica
    @Query("SELECT m FROM Media m JOIN m.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomType = :roomType")
    List<Media> getAllMediasByHotelIdAndRoomType(@Param("hotelId") Long hotelId, @Param("roomType") String roomType);

    // Metodo personalizado para encontrar todas las amenities de una habitacion específica
 //   @Query("SELECT a FROM Amenity a JOIN a.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomId = :roomId")
//    @Query("SELECT a FROM Amenity a JOIN a.rooms r WHERE r.roomId = :roomId AND r.hotel.hotelId = :hotelId")
//    List<Amenity> getAllAmenitiesByHotelIdAndRoomId(Long hotelId, Long roomId);

    @Query("SELECT a FROM Amenity a JOIN a.rooms r WHERE r.roomId = :roomId AND r.hotel.hotelId = :hotelId")
    List<Amenity> getAllAmenitiesByHotelIdAndRoomId(@Param("hotelId") Long hotelId, @Param("roomId") Long roomId);

    // Metodo personalizado para encontrar todas las amenities de un tipo de habitacion específica
    @Query("SELECT a FROM Amenity a JOIN a.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomType = :roomType")
    List<Amenity> getAllAmenitiesByHotelIdAndRoomType(Long hotelId, String roomType);
}
