package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
@Tag(name = "Media Management", description = "Endpoints para gestión de medias de hoteles y habitaciones")

public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // Create a new Media
    @PostMapping
    @Operation(summary = "Crea una media", description = "Crea una media.")
    public ResponseEntity<Media> createMedia(
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) Long roomId,
            @RequestBody Media media) {
        if (hotelId == null && roomId == null) {
            throw new IllegalArgumentException("Either hotelId or roomId must be provided");
        }
        Media createdMedia = mediaService.createMedia(media, hotelId, roomId);
        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
    }

    // Get all Media
    @GetMapping
    @Operation(summary = "Consulta todas las medias", description = "Consulta todas las medias.")
    public ResponseEntity<List<Media>> getAllMedia() {
        List<Media> mediaList = mediaService.getAllMedia();
        return new ResponseEntity<>(mediaList, HttpStatus.OK);
    }

    // Get Media by ID
    @GetMapping("/{id}")
    @Operation(summary = "Consulta una media", description = "Consulta una media a través de su id.")
    public ResponseEntity<Media> getMediaById(@PathVariable Long id) {
        return mediaService.getMediaById(id)
                .map(media -> new ResponseEntity<>(media, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));
    }

//    // Get all Media for a specific Room
//    @GetMapping("/room/{roomId}")
//    @Operation(summary = "Consulta una media", description = "Consulta una media de una room unit.")
//    public ResponseEntity<List<Media>> getAllMediaByRoomId(@PathVariable Long roomId) {
//        List<Media> mediaList = mediaService.getAllMediaByRoomId(roomId);
//        return new ResponseEntity<>(mediaList, HttpStatus.OK);
//    }
//
//    // Get all Media for a specific Hotel
//    @GetMapping("/hotel/{hotelId}")
//    @Operation(summary = "Consulta todas las media", description = "Consulta todas las medias de un hotel por el hotelId.")
//    public ResponseEntity<List<Media>> getAllMediaByHotelId(@PathVariable Long hotelId) {
//        List<Media> mediaList = mediaService.getAllMediaByHotelId(hotelId);
//        return new ResponseEntity<>(mediaList, HttpStatus.OK);
//    }

    // Update Media
    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una media", description = "Actualiza una media por la mediaId.")
    public ResponseEntity<Media> updateMedia(@PathVariable Long id, @RequestBody Media mediaDetails) {
        Media updatedMedia = mediaService.updateMedia(id, mediaDetails);
        return new ResponseEntity<>(updatedMedia, HttpStatus.OK);
    }

    // Delete Media
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una media", description = "Elimina una media por la mediaId.")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/room/{roomId}/media/{mediaId}")
    @Operation(summary = "Elimina una media", description = "Elimina o desconecta una media de una habitación.")
    public ResponseEntity<Void> removeMediaFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long mediaId) {
        mediaService.removeMediaFromRoom(roomId, mediaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/hotel/{hotelId}/media/{mediaId}")
    @Operation(summary = "Elimina una media", description = "Elimina o desconecta una media de un hotel.")
    public ResponseEntity<Void> removeAmenityFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long mediaId) {
        mediaService.removeMediaFromHotel(hotelId, mediaId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}