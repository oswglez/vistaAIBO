package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.service.RoomService;
import com.expectra.roombooking.service.HotelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Room Management", description = "Endpoints para gestión de habitaciones")
public class RoomController {

    private final RoomService roomService;
    private final HotelService hotelService;

    @Autowired
    public RoomController(RoomService roomService, HotelService hotelService) {
        this.roomService = roomService;
        this.hotelService = hotelService;
    }

    // Create a new Room
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestParam Long hotelId, @RequestBody Room room) {
        Hotel hotel = hotelService.findHotelById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        room.setHotel(hotel);
        Room savedRoom = roomService.saveRoom(room);
        return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
    }

    // Get Room by ID
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(room -> new ResponseEntity<>(room, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails) {
        Room existingRoom = roomService.getRoomById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        // Actualizar los campos necesarios
        existingRoom.setRoomNumber(roomDetails.getRoomNumber());
        existingRoom.setRoomType(roomDetails.getRoomType());
        existingRoom.setRoomName(roomDetails.getRoomName());
        existingRoom.setAmenities(roomDetails.getAmenities());
        existingRoom.setMedia(roomDetails.getMedia());

        Room updatedRoom = roomService.saveRoom(existingRoom);
        return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
    }

    // Delete Room
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoomById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get Room Media by Hotel and Room ID
    @GetMapping("/{hotelId}/{roomId}/media")
    public ResponseEntity<List<Media>> getRoomMedia(@PathVariable Long hotelId, @PathVariable Long roomId) {
        List<Media> media = roomService.getRoomMediaByHotelAndRoom(hotelId, roomId);
        return new ResponseEntity<>(media, HttpStatus.OK);
    }

    // Get Room Amenities by Hotel and Room ID
    @GetMapping("/{hotelId}/{roomId}/amenities")
    public ResponseEntity<List<Amenity>> getRoomAmenities(@PathVariable Long hotelId, @PathVariable Long roomId) {
        List<Amenity> amenities = roomService.getRoomAmenitiesByHotelAndRoom(hotelId, roomId);
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }

    // Get Room Media by Hotel ID and Room Type
    @GetMapping("/{hotelId}/type/{roomType}/media")
    public ResponseEntity<List<Media>> getRoomMediaByType(
            @PathVariable Long hotelId,
            @PathVariable String roomType) {
        List<Media> media = roomService.getRoomMediaByHotelAndType(hotelId, roomType);
        return new ResponseEntity<>(media, HttpStatus.OK);
    }

    // Get Room Amenities by Hotel ID and Room Type
    @GetMapping("/{hotelId}/type/{roomType}/amenities")
    public ResponseEntity<List<Amenity>> getRoomAmenitiesByType(
            @PathVariable Long hotelId,
            @PathVariable String roomType) {
        List<Amenity> amenities = roomService.getRoomAmenitiesByHotelAndType(hotelId, roomType);
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }
}
