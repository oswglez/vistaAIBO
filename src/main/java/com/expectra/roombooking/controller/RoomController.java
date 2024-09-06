package com.expectra.roombooking.controller;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/{roomId}")
    public ResponseEntity<Optional<Room>> getRoomById(
            @PathVariable Long roomId) {
        Optional<Room> room = roomService.getRoomById(roomId);
        if (room.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(room);
    }

    @GetMapping("{hotelId}/{roomType}/media")
    public ResponseEntity<List<Media>> getMediaByRoomType(
            @PathVariable Long hotelId,
            @PathVariable String roomType) {
        List<Media> medias = roomService.getMediaByRoomType(hotelId, roomType);
        if (medias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(medias);
    }

    @GetMapping("{hotelId}/type/{roomType}/amenity")
    public ResponseEntity<List<Amenity>> getRoomAmenities(
            @PathVariable Long hotelId,
            @PathVariable String roomType) {
        List<Amenity> amenities = roomService.getAmenitiesByHotelIdAndRoomType(hotelId, roomType);
        if (amenities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(amenities);
    }


    @GetMapping("{hotelId}/media/{roomId}")
    public ResponseEntity<List<Media>> getMediaByRoomId(
            @PathVariable Long hotelId,
            @PathVariable Long roomId) {
        List<Media> medias = roomService.getAllMediasByHotelIdAndRoomId(hotelId, roomId);
        if (medias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(medias);
    }

    @GetMapping("{hotelId}/amenity/{roomId}")
    public ResponseEntity<List<Amenity>> getAmenitiesByRoomId(
            @PathVariable Long hotelId,
            @PathVariable Long roomId) {
        List<Amenity> amenities = roomService.getAmenitiesByHotelIdAndRoomId(hotelId, roomId);
        if (amenities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(amenities);
    }
    // Otros métodos...

}