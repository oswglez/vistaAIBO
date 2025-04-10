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
@RequestMapping("/api/medias")
@Tag(name = "Media Management", description = "Endpoints para gestión de medias de hoteles y habitaciones")
@CrossOrigin(origins = "http://localhost:5173")

public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(final MediaService mediaService) {
        this.mediaService = mediaService;
    }
    private final String mediaNotFound = "Media not found with ID: ";

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
//    // Crear media asociada a un hotel
//    @PostMapping("/hotels/{hotelId}")
//    @Operation(summary = "Crear media para un hotel", description = "Crea un recurso multimedia y lo asocia a un hotel.")
//    public ResponseEntity<Media> createHotelMedia(
//            @PathVariable Long hotelId,
//            @RequestBody Media media) {
//        Media createdMedia = mediaService.createMediaForHotel(hotelId, media);
//        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
//    }
//
//    // Crear media asociada a una habitación
//    @PostMapping("/rooms/{roomId}")
//    @Operation(summary = "Crear media para una habitación", description = "Crea un recurso multimedia y lo asocia a una habitación.")
//    public ResponseEntity<Media> createRoomMedia(
//            @PathVariable Long roomId,
//            @RequestBody Media media) {
//        Media createdMedia = mediaService.createMediaForRoom(roomId, media);
//        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
//    }
    // Get all Media
    @GetMapping
    @Operation(summary = "Consulta todas las medias", description = "Consulta todas las medias.")
    public ResponseEntity<List<Media>> getAllMedia() {
        List<Media> mediaList = mediaService.getAllMedia();
        return ResponseEntity.ok(mediaList);
    }

    // Get Media by ID
    @GetMapping("/{mediaId}")
    @Operation(summary = "Consulta una media por su Id", description = "Consulta una media a través de su id.")
    public ResponseEntity<Media> getMediaById(@PathVariable Long mediaId) {
        return mediaService.getMediaById(mediaId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(mediaNotFound + mediaId));
    }


    // Update Media
    @PutMapping("/{mediaId}")
    @Operation(summary = "Actualiza una media", description = "Actualiza una media por la mediaId.")
    public ResponseEntity<Media> updateMedia(@PathVariable Long mediaId, @RequestBody Media mediaDetails) {
        Media updatedMedia = mediaService.updateMedia(mediaId, mediaDetails);
        return ResponseEntity.ok(updatedMedia);
    }

    // Delete Media
    @DeleteMapping("/{mediaId}")
    @Operation(summary = "Elimina una media", description = "Elimina una media por la mediaId.")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long mediaId) {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rooms/{roomId}/media/{mediaId}")
    @Operation(summary = "Remueve una media de una habitacion", description = "Elimina o desconecta una media de una habitación.")
    public ResponseEntity<Void> removeMediaFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long mediaId) {
        mediaService.removeMediaFromRoom(roomId, mediaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hotels/{hotelId}/media/{mediaId}")
    @Operation(summary = "Remueve una media de un hotel", description = "Elimina o desconecta una media de un hotel.")
    public ResponseEntity<Void> removeAmenityFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long mediaId) {
        mediaService.removeMediaFromHotel(hotelId, mediaId);
        return ResponseEntity.noContent().build();
    }
}