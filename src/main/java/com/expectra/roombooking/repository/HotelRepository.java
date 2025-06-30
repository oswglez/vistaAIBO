package com.expectra.roombooking.repository;

import com.expectra.roombooking.dto.HotelListDTO;
import com.expectra.roombooking.model.*;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Custom method to find hotels by name
    List<Hotel> findByHotelName(String hotelName);

    @Query("SELECT h FROM Hotel h WHERE h.hotelDeleted = false")
    List<Hotel> findAllHotels();

    // Custom method to logically delete a hotel
    @Transactional
    @Modifying
    @Query("UPDATE Hotel h SET h.hotelDeleted = true WHERE h.hotelId = :hotelId")
    void markHotelAsDeleted(@Param("hotelId") Long hotelId);

    // Custom method to find all amenities for a specific hotel
    @Query("SELECT a FROM Amenity a JOIN a.hotels h WHERE h.hotelId = :hotelId")
    List<Amenity> findAllAmenitiesByHotelId(@NonNull Long hotelId);

    @Query("SELECT m FROM Media m JOIN m.hotels h WHERE h.hotelId = :hotelId")
    List<Media> findAllMediasByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId")
    List<Room> findAllRoomsByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT c FROM Contact c JOIN c.hotels h WHERE h.hotelId = :hotelId")
    List<Contact> findAllContactsByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT a FROM Address a WHERE a.hotel.hotelId = :hotelId")
    List<Address> findAllAddressesByHotelId(@Param("hotelId") @NonNull Long hotelId);

    @Query("SELECT h FROM Hotel h LEFT JOIN FETCH h.rooms r WHERE h.hotelId = :hotelId AND r.roomId IN (SELECT r2.roomId FROM Room r2 WHERE r2.hotel = h AND r2.roomType = :roomType)")
    Optional<Hotel> findHotelAndRoomsByHotelIdAndRoomType(
            @Param("hotelId") @NonNull Long hotelId,
            @Param("roomType") @NonNull String roomType);

    @Query("SELECT new com.expectra.roombooking.dto.HotelListDTO(" +
            "    h.hotelId, " +
            "    h.hotelCode AS hotelCode, " +
            "    h.hotelName AS hotelName, " +
            "    h.hotelWebsiteUrl AS hotelWebsiteUrl, " +
            "    h.hotelStatus AS hotelStatus, " +
            "    b.brandName AS hotelBrand, " +
            "    c.chainName AS hotelChain, " +
            "    a.street AS hotelStreet, " +
            "    a.city AS hotelCity, " +
            "    a.state AS hotelState, " +
            "    a.country AS hotelCountry, " +
            "    ct.firstName AS contactFirstName, " +
            "    ct.lastName AS contactLastName, " +
            "    ct.contactTitle AS contactTitle" +
            ") " +
            "FROM Hotel h " +
            "LEFT JOIN h.brand b " +
            "LEFT JOIN b.chain c " +
            "LEFT JOIN h.addresses a WITH a.addressType = 'MAIN' " +
            "LEFT JOIN h.contacts ct WITH ct.contactType = 'MAIN' " +
            "JOIN UserHotelRole uhr ON uhr.hotel = h " +
            "JOIN uhr.user u " +
            "WHERE h.hotelDeleted = false AND uhr.user.userId = :userId")
    Page<HotelListDTO> findConsolidatedHotelData(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT h FROM Hotel h " +
            "LEFT JOIN FETCH h.brand b " +
            "LEFT JOIN FETCH b.chain " +
            "LEFT JOIN FETCH h.contacts " +
            "LEFT JOIN FETCH h.addresses " +
            "WHERE h.hotelId = :hotelId")
    Optional<Hotel> findHotelByIdWithFullRelations(@Param("hotelId") Long id);
}
