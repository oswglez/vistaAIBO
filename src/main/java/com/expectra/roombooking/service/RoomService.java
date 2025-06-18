package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
// RoomServiceImpl.java
// RoomService.java

public interface RoomService {
    // Basic CRUD operations
    Room saveRoom(Room room);
    Optional<Room> getRoomById(Long roomId);
    void deleteRoomById(Long roomId);
    Page<Room> getAllRooms(Pageable pageable);
    Page<Room> getRoomsByHotelId(Long hotelId, Pageable pageable);

    // Media related operations
    List<Media> getAllMediasByHotelIdAndRoomId(Long hotelId, Long roomId);
    List<Media> getRoomMediaByHotelAndRoomType(Long hotelId, String roomType);

    // Amenity related operations
    List<Amenity> getAllAmenitiesByHotelIdAndRoomId(Long hotelId, Long roomId);
    List<Amenity> getRoomAmenitiesByHotelAndRoomType(Long hotelId, String roomType);
}