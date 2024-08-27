package com.expectra.roombooking.controller;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.RoomRepository;
import com.expectra.roombooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "room")
public class RoomController {
    private final RoomRepository roomRepository;
    @Value("${spring.application.name}")
    String appName;

    @Autowired
    RoomService roomService;


    public RoomController(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Endpoint para obtener habitaciones por hotelId y roomType
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(
            @RequestParam Long hotelId,
            @RequestParam Long roomId) {

        List<Room> rooms = roomService.findAllAvailableRooms(hotelId, roomId);
        if (rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(rooms);
        }
    }

    @GetMapping("/{roomType}/media")
    public ResponseEntity<List<Media>> getMediaByRoomType(
            @RequestParam Long hotelId,
            @RequestParam String roomType) {
        List<Media> mediaList = roomService.getMediaByRoomType(hotelId, roomType);
        if (mediaList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mediaList);
    }


    @GetMapping("{hotelId}/{roomId}/media")
    public ResponseEntity<List<Media>> getMediaByHotelIdAndRoomId(
            @RequestParam Long hotelId,
            @RequestParam Long roomId) {

        List<Media> medias = roomService.getMediaByHotelIdAndRoomId(hotelId, roomId);
        if (medias.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(medias);
        }
    }


    @GetMapping("{hotelId}/{roomId}/amenity")
    public ResponseEntity<List<Amenity>> getRoomMedias(
            @RequestParam Long hotelId,
            @RequestParam Long roomId) {

        List<Amenity> amenities = roomService.getAmenitiesByHotelIdAndRoomId(hotelId, roomId);
        if (amenities.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(amenities);
        }
    }


    @GetMapping("{hotelId}/{roomType}/amenity")
    public ResponseEntity<List<Amenity>> getRoomMedias(
            @RequestParam Long hotelId,
            @RequestParam String roomType) {

        List<Amenity> amenities = roomService.getAmenitiesByHotelIdAndRoomType(hotelId, roomType);
        if (amenities.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(amenities);
        }
    }

}