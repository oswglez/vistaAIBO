package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.*;
import com.expectra.roombooking.service.RoomService;
import com.expectra.roombooking.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Room Management", description = "Endpoints for room management")

public class RoomController {

    private final RoomService roomService;
    private final HotelService hotelService;
    private final String messageNotfound = "Room not found by Id" ;


    @Autowired
    public RoomController(final RoomService roomService, final HotelService hotelService) {
        this.roomService = roomService;
        this.hotelService = hotelService;
    }

    // Create a new Room
    @PostMapping("/{hotelId}")
    @Operation(summary = "Create a room", description = "Creates a room for a hotel.")
    public ResponseEntity<Room> createRoom(@PathVariable Long hotelId, @RequestBody Room room) {
        Hotel hotel = hotelService.findHotelById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        room.setHotel(hotel);
        Room savedRoom = roomService.saveRoom(room);
        return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
    }

    // Get Room by ID
    @GetMapping("/{roomId}")
    @Operation(summary = "Get a room by id", description = "Retrieves a room by its Id.")
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
        return roomService.getRoomById(roomId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotfound + roomId));
    }
    @PutMapping("/{roomId}")
    @Operation(summary = "Update a room", description = "Updates room data.")
    public ResponseEntity<Room> updateRoom(@PathVariable Long roomId, @RequestBody Room roomDetails) {
        Room existingRoom = roomService.getRoomById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotfound + roomId));

        // Actualizar los campos necesarios
        existingRoom.setRoomNumber(roomDetails.getRoomNumber());
        existingRoom.setRoomType(roomDetails.getRoomType());
        existingRoom.setRoomName(roomDetails.getRoomName());
        existingRoom.setAmenities(roomDetails.getAmenities());
        existingRoom.setMedias(roomDetails.getMedias());

        Room updatedRoom = roomService.saveRoom(existingRoom);
        return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
    }

    // Delete Room
    @DeleteMapping("/{roomId}")
    @Operation(summary = "Delete a room", description = "Deletes a room and disconnects its media and amenities.")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoomById(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // Get Room Media by Hotel ID and Room Unit
    @GetMapping("/{hotelId}/roomId/{roomId}/media")
    @Operation(summary = "Get media by roomId", description = "Retrieves media for a hotel room.")
    public ResponseEntity<List<Media>> getRoomMediasByHotelIdAndRoomId(
            @PathVariable Long hotelId,
            @PathVariable Long roomId) {
        List<Media> medias = roomService.getAllMediasByHotelIdAndRoomId(hotelId, roomId);
        return new ResponseEntity<>(medias, HttpStatus.OK);
    }

    // Get Room Amenities by Hotel ID and Room Type
    @GetMapping("/{hotelId}/roomId/{roomId}/amenities")
    @Operation(summary = "Get amenities by roomId", description = "Retrieves amenities for a hotel room.")
    public ResponseEntity<List<Amenity>> getRoomAmenitiesByHotelIdAndRoomId(
            @PathVariable Long hotelId,
            @PathVariable Long roomId) {
        List<Amenity> amenities = roomService.getAllAmenitiesByHotelIdAndRoomId(hotelId, roomId);
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }
    // Get Room Media by Hotel ID and Room Type
    @GetMapping("/{hotelId}/type/{roomType}/media")
    @Operation(summary = "Get media by room type", description = "Retrieves media for a specific room type in a hotel.")
    public ResponseEntity<List<Media>> getRoomMediaByType(
            @PathVariable Long hotelId,
            @PathVariable String roomType) {
        try {
            String type = roomType.toUpperCase();
            List<Media> media = roomService.getRoomMediaByHotelAndRoomType(hotelId, type);
            return new ResponseEntity<>(media, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // If the value is not valid, returns 400
        }
    }

    // Get Room Amenities by Hotel ID and Room Type
    @GetMapping("/{hotelId}/type/{roomType}/amenities")
    @Operation(summary = "Get amenities by room type", description = "Retrieves amenities for a specific room type in a hotel.")
    public ResponseEntity<List<Amenity>> getRoomAmenitiesByType(
            @PathVariable Long hotelId,
            @PathVariable String roomType) {
        try {
            String type = roomType.toUpperCase(); // Convierte String a Enum
            List<Amenity> amenities = roomService.getRoomAmenitiesByHotelAndRoomType(hotelId, type);
            return new ResponseEntity<>(amenities, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // If the value is not valid, returns 400
        }
    }
}
