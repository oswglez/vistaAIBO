package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.HotelCreationRequestDTO;
import com.expectra.roombooking.dto.HotelDTO;
import com.expectra.roombooking.dto.HotelListDTO;
import com.expectra.roombooking.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface HotelService {
    // Basic CRUD operations
    List<Hotel> findAllHotels();
    Optional<Hotel> findHotelById(Long hotelId);
    Hotel saveHotel(Hotel hotel);
    Hotel createHotelWithDetails(HotelCreationRequestDTO hotelRequest);
    void deleteHotelById(Long hotelId);
    void logicalDeleteHotel(Long hotelId);
    // Specific operations
    List<Hotel> findHotelsByName(String hotelName);
    List<Amenity> findHotelAmenities(Long hotelId);
    List<Media> findHotelMedias(Long hotelId);
    List<Room> findHotelRooms(Long hotelId);
    List<Contact> findAllContactsByHotelId(Long hotelId);
    List<Address> findAllAddressesByHotelId(Long hotelId);
    HotelDTO getHotelAndRoomsByHotelId(Long hotelId);
    HotelDTO findHotelAndRoomsByHotelIdAndRoomType(Long hotelId, String roomTypes);
    Page<HotelListDTO> findConsolidatedHotelData(Long userId, Pageable pageable);
    HotelCreationRequestDTO findHotelByIdWithFullRelations(Long HotelId);
    HotelCreationRequestDTO updateHotelWithDetails(Long hotelId, HotelCreationRequestDTO hotelDetails);

    //   List<String> getFloorPlansByHotelId(Long hotelId);

}