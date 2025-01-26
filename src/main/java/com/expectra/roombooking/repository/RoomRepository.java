package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // Metodo para guardar una habitación (crear o actualizar)
    @Override
    <S extends Room> S save(S entity);

    // Metodo para eliminar una habitación por entidad
    @Override
    void delete(Room entity);

    // Metodo para eliminar una habitación por ID
    @Override
    void deleteById(Long roomId);


    // Metodo personalizado para encontrar todas las medias de una habitacion específica
    @Query("SELECT m FROM Media m JOIN m.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomId = :roomId")
    List<Media> getAllMediasByHotelIdAndRoomId(@Param("hotelId") Long hotelId, @Param("roomId") Long roomId);

    // Metodo personalizado para encontrar todas las amenities de una habitacion específica
    @Query("SELECT a FROM Amenity a JOIN a.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomId = :roomId")
    List<Amenity> getAllAmenitiesByHotelIdAndRoomId(Long hotelId, Long roomId);

    // Metodo personalizado para encontrar todas las medias de un tipo de habitacion específica
    @Query("SELECT m FROM Media m JOIN m.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomType = :roomType")
    List<Media> getAllMediasByHotelIdAndRoomType(@Param("hotelId") Long hotelId, @Param("roomType") String roomType);

    // Metodo personalizado para encontrar todas las amenities de un tipo de habitacion específica
    @Query("SELECT a FROM Amenity a JOIN a.rooms r JOIN r.hotel h WHERE h.hotelId = :hotelId AND r.roomType = :roomType")
    List<Amenity> getAllAmenitiesByHotelIdAndRoomType(Long hotelId, String roomType);
}
