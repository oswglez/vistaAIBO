package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;

import java.util.List;
import java.util.Optional;
// RoomServiceImpl.java
// RoomService.java

public interface RoomService {
    Room saveRoom(Room room);
    void deleteRoomById(Long roomId);
    List<Media> getRoomMediaByHotelAndRoomType(Long hotelId, String roomType);
    List<Amenity> getRoomAmenitiesByHotelAndRoomType(Long hotelId, String roomType);
    Optional<Room> getRoomById(Long roomId);
    List<Media> getAllMediasByHotelIdAndRoomId(Long hotelId, Long roomId);
    List<Amenity> getAllAmenitiesByHotelIdAndRoomId(Long hotelId, Long roomId);
}