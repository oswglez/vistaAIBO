package com.expectra.roombooking.service;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.RoomRepository;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
// RoomServiceImpl.java
// RoomService.java

public interface RoomService {
    Room saveRoom(Room room);
//    void deleteRoom(Room room);
    void deleteRoomById(Long roomId);
//    List<Room> getRoomsByHotelId(Long hotelId);
    List<Media> getRoomMediaByHotelAndRoom(Long hotelId, Long roomId);
//    List<Amenity> getRoomAmenitiesByHotelAndRoom(Long hotelId, Long roomId);
    List<Media> getRoomMediaByHotelAndType(Long hotelId, String roomType);
    List<Amenity> getRoomAmenitiesByHotelAndType(Long hotelId, String roomType);
    Optional<Room> getRoomById(Long roomId);
  //  List<Media> getAllMediasByHotelIdAndRoomId(Long hotelId, Long roomId);

}
