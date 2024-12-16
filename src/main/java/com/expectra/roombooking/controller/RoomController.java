package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.service.RoomService;
import com.expectra.roombooking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
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
        hotelService.getHotelById(hotelId).orElseThrow(() ->
                new ResourceNotFoundException("Hotel not found with id: " + hotelId));

//        room.setHotelId(hotelId); // Assuming Room has a hotelId field
        Room createdRoom = roomService.createRoom(room);
        return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
    }

    // Get all Rooms
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    // Get Room by ID
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(room -> new ResponseEntity<>(room, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }

    // Update Room
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails) {
        Room updatedRoom = roomService.updateRoom(id, roomDetails);
        return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
    }

    // Delete Room
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get Rooms by Hotel ID                                    **DESARROLLAR **
//    @GetMapping("/hotel/{hotelId}")
//    public ResponseEntity<List<Room>> getRoomsByHotelId(@PathVariable Long hotelId) {
//        hotelService.getHotelById(hotelId).orElseThrow(() ->
//                new ResourceNotFoundException("Hotel not found with id: " + hotelId));
//
//        List<Room> rooms = roomService.getRoomsByHotelId(hotelId);
//        return new ResponseEntity<>(rooms, HttpStatus.OK);
//    }
}
