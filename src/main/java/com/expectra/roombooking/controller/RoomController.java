package com.expectra.roombooking.controller;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
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

    @Autowired
    RoomService roomService;


    // Métodos...

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

    // Otros métodos...

}