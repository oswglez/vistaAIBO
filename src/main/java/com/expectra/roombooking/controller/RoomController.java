package com.expectra.roombooking.controller;

import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.model.Video;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.RoomRepository;
import com.expectra.roombooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.Optional;

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

//    @PostMapping("/RestGetPersonaPorDocumento")
//    public ResponseEntity<RestGetPersonaPorDocumentoResponseDto> restGetPersonaPorDocumento(
//            @Valid @RequestBody RestGetPersonaPorDocumentoRequestDto request)
//            throws UserNotFoundException, BadRequestException {
//        RestGetPersonaPorDocumentoResponseDto response = service.restGetPersonaPorDocumento(request);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
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

    @GetMapping("/{roomCode}/media")
    public ResponseEntity<List<Video>> getMediaByRoomCode(
            @RequestParam Long hotelId,
            @RequestParam Integer roomCode) {
        List<Video> mediaList = roomService.getMediaByRoomCode(hotelId, roomCode);
        if (mediaList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mediaList);
    }


    @GetMapping("/{roomType}/media")
    public ResponseEntity<List<Room>> getRoomMedias(
            @RequestParam Long hotelId,
            @RequestParam Integer roomCode) {

        List<Room> rooms = roomService.getRoomMedias(hotelId, roomCode);
        if (rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(rooms);
        }
    }
    @GetMapping("/amenities")
    public ResponseEntity<List<Room>> getAmenitiesRooms(
            @RequestParam Long hotelId,
            @RequestParam Integer roomCode) {

        List<Room> rooms = roomService.findRoomAmenities(hotelId, roomCode);
        if (rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(rooms);
        }
    }

}