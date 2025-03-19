package com.expectra.roombooking.repository;

import com.expectra.roombooking.model.*;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {


    // Metodo personalizado para buscar hoteles por nombre
    List<Hotel> findByHotelName(String hotelName);

    // Metodo personalizado para encontrar todas las amenities de un hotel específico
    @Query("SELECT a FROM Amenity a JOIN a.hotels h WHERE h.hotelId = :hotelId")
    List<Amenity> findAllAmenitiesByHotelId(@NonNull Long hotelId);

    @Query("SELECT m FROM Media m JOIN m.hotels h WHERE h.hotelId = :hotelId")
    List<Media> findAllMediasByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId")
    List<Room> findAllRoomsByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT c FROM Contact c JOIN c.hotels h WHERE h.hotelId = :hotelId")
    List<Contact> findAllContactsByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT a FROM Address a JOIN a.hotels h WHERE h.hotelId = :hotelId")
    List<Address> findAllAddressesByHotelId(@Param("hotelId") @NonNull Long hotelId);

   @Query("SELECT h FROM Hotel h JOIN FETCH h.rooms WHERE h.hotelId = :hotelId")
    Optional<Hotel> getHotelAndRoomsByHotelId(@Param("hotelId") @NonNull Long hotelId);

//    @Query("SELECT h FROM Hotel h JOIN FETCH h.rooms r WHERE h.hotelId = :hotelId AND r.roomType = :roomType")
    @Query("SELECT h FROM Hotel h JOIN FETCH h.rooms r WHERE h.hotelId = :hotelId AND r.roomId IN (SELECT r2.roomId FROM Room r2 WHERE r2.hotel = h AND r2.roomType = :roomType)")
    Optional<Hotel>  findHotelAndRoomsByHotelIdAndRoomType(
            @Param("hotelId") @NonNull Long hotelId,
            @Param("roomType") @NonNull RoomType roomType);
}

