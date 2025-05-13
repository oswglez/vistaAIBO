package com.expectra.roombooking.repository;

import com.expectra.roombooking.dto.HotelListDTO;
import com.expectra.roombooking.model.*;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("SELECT h FROM Hotel h WHERE h.hotelId = :hotelId AND h.hotelDeleted = false")
    Optional<Hotel> findHotelById(@Param("hotelId") Long hotelId);

    @Query("SELECT h FROM Hotel h LEFT JOIN FETCH h.rooms WHERE h.hotelId = :hotelId AND h.hotelDeleted = false")
    Optional<Hotel> getHotelAndRoomsByHotelId(@Param("hotelId") @NonNull Long hotelId);
    // Metodo personalizado para buscar hoteles por nombre
    List<Hotel> findByHotelName(String hotelName);

    @Query("SELECT h FROM Hotel h WHERE h.hotelDeleted = false")
    List<Hotel> findAllHotels();

    // Metodo personalizado para buscar hoteles por nombre
    @Transactional
    @Modifying
    @Query("UPDATE Hotel h SET h.hotelDeleted = true WHERE h.hotelId = :hotelId")
    void markHotelAsDeleted(@Param("hotelId") Long hotelId);

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


//    @Query("SELECT h FROM Hotel h JOIN FETCH h.rooms r WHERE h.hotelId = :hotelId AND r.roomType = :roomType")
    @Query("SELECT h FROM Hotel h LEFT JOIN FETCH h.rooms r WHERE h.hotelId = :hotelId AND r.roomId IN (SELECT r2.roomId FROM Room r2 WHERE r2.hotel = h AND r2.roomType = :roomType)")
    Optional<Hotel>  findHotelAndRoomsByHotelIdAndRoomType(
            @Param("hotelId") @NonNull Long hotelId,
            @Param("roomType") @NonNull String roomType);

    @Query("SELECT new com.expectra.roombooking.dto.HotelListDTO(" +
            "    h.hotelId, " +
            "    h.hotelCode, " +         // Asume que la entidad Hotel tiene un campo 'hotelCode'
            "    h.hotelName, " +
            "    h.hotelWebsiteUrl, " +      // Asume que la entidad Hotel tiene un campo 'websiteUrl'
            "    h.hotelStatus, " +            // Asume que la entidad Hotel tiene un campo 'status'
            "    b.brandName, " +         // Asume que la entidad Hotel tiene 'brand' y Brand tiene 'brandName'
            "    c.chainName, " +         // Asume que Brand tiene 'chain' y Chain tiene 'chainName'
            "    a.street, " +     // Asume que Hotel tiene 'addresses' y Address tiene 'streetAddress'
            "    a.city, " +
            "    a.state, " +     // Asume que Address tiene 'stateProvince'
            "    a.country, " +
            "    ct.firstName, " +        // Asume que Hotel tiene 'contacts' y Contact tiene 'firstName'
            "    ct.lastName, " +         // y 'lastName'
            "    ct.contactTitle " +             // y 'title'
            ") " +
            "FROM Hotel h " + // 'Hotel' es el nombre de tu Entidad JPA
            "LEFT JOIN h.brand b " +     // 'brand' es el campo de relación en la entidad Hotel
            "LEFT JOIN b.chain c " +     // 'chain' es el campo de relación en la entidad Brand
            "LEFT JOIN h.addresses a WITH a.addressType = 'MAIN' " + // 'addresses' es la colección en Hotel, filtra por tipo
            "LEFT JOIN h.contacts ct WITH ct.contactType = 'MAIN' " + // 'contacts' es la colección en Hotel, filtra por tipo
            // "WHERE h.user = :user " + // Ejemplo si filtras por usuario y Hotel tiene una relación 'user'
            "ORDER BY h.hotelId ASC")
    List<HotelListDTO> findConsolidatedHotelData(); // Ajusta parámetros si es necesario (ej: Pageable, @Param("user") UserEntity user)

}