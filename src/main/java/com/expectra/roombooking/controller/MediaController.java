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
import java.util.Map;

@RestController
@RequestMapping("/api/medias")
@Tag(name = "Media Management", description = "Endpoints for managing hotel and room media")
@CrossOrigin(origins = "*")

public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(final MediaService mediaService) {
        this.mediaService = mediaService;
    }
    private final String mediaNotFound = "Media not found with ID: ";

    // Create a new Media
    @PostMapping
    @Operation(summary = "Create a media", description = "Creates a media.")
    public ResponseEntity<Media> createMedia(
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) Long roomId,
            @RequestBody Map<String, Object> requestBody) {

        if (hotelId == null && roomId == null) {
            throw new IllegalArgumentException("Either hotelId or roomId must be provided");
        }

        // Create the Media entity and assign values
        Media media = new Media();
        media.setMediaCode((Integer) requestBody.get("mediaCode"));
        media.setMediaDescription((String) requestBody.get("mediaDescription"));
        media.setMediaUrl((String) requestBody.get("mediaUrl"));
        media.setMediaType((String) requestBody.get("mediaType"));
//        // String to enum MediaType conversion
//        String mediaTypeStr = (String) requestBody.get("mediaType");
//        media.setMediaType(MediaType.valueOf(mediaTypeStr));

        // Call the service
        Media createdMedia = mediaService.createMedia(media, hotelId, roomId);
        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
    }



//    @PostMapping
//    @Operation(summary = "Create a media", description = "Creates a media.")
//    public ResponseEntity<Media> createMedia(
//            @RequestParam(required = false) Long hotelId,
//            @RequestParam(required = false) Long roomId,
//            @RequestBody Media media) {
//        if (hotelId == null && roomId == null) {
//            throw new IllegalArgumentException("Either hotelId or roomId must be provided");
//        }
//        Media createdMedia = mediaService.createMedia(media, hotelId, roomId);
//        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
//    }
//    // Create media associated with a hotel
//    @PostMapping("/hotels/{hotelId}")
//    @Operation(summary = "Create media for a hotel", description = "Creates a media resource and associates it with a hotel.")
//    public ResponseEntity<Media> createHotelMedia(
//            @PathVariable Long hotelId,
//            @RequestBody Media media) {
//        Media createdMedia = mediaService.createMediaForHotel(hotelId, media);
//        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
//    }
//
//    // Create media associated with a room
//    @PostMapping("/rooms/{roomId}")
//    @Operation(summary = "Create media for a room", description = "Creates a media resource and associates it with a room.")
//    public ResponseEntity<Media> createRoomMedia(
//            @PathVariable Long roomId,
//            @RequestBody Media media) {
//        Media createdMedia = mediaService.createMediaForRoom(roomId, media);
//        return new ResponseEntity<>(createdMedia, HttpStatus.CREATED);
//    }
    // Get all Media
    @GetMapping
    @Operation(summary = "Get all media", description = "Retrieves all media.")
    public ResponseEntity<List<Media>> getAllMedia() {
        List<Media> mediaList = mediaService.getAllMedia();
        return ResponseEntity.ok(mediaList);
    }

    // Get Media by ID
    @GetMapping("/{mediaId}")
    @Operation(summary = "Get media by Id", description = "Retrieves media by its id.")
    public ResponseEntity<Media> getMediaById(@PathVariable Long mediaId) {
        return mediaService.getMediaById(mediaId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(mediaNotFound + mediaId));
    }


    // Update Media
    @PutMapping("/{mediaId}")
    @Operation(summary = "Update a media", description = "Updates a media by its mediaId.")
    public ResponseEntity<Media> updateMedia(@PathVariable Long mediaId, @RequestBody Media mediaDetails) {
        Media updatedMedia = mediaService.updateMedia(mediaId, mediaDetails);
        return ResponseEntity.ok(updatedMedia);
    }

    // Delete Media
    @DeleteMapping("/{mediaId}")
    @Operation(summary = "Delete a media", description = "Deletes a media by its mediaId.")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long mediaId) {
        mediaService.deleteMedia(mediaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rooms/{roomId}/media/{mediaId}")
    @Operation(summary = "Remove media from a room", description = "Deletes or disconnects media from a room.")
    public ResponseEntity<Void> removeMediaFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long mediaId) {
        mediaService.removeMediaFromRoom(roomId, mediaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hotels/{hotelId}/media/{mediaId}")
    @Operation(summary = "Remove media from a hotel", description = "Deletes or disconnects media from a hotel.")
    public ResponseEntity<Void> removeAmenityFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long mediaId) {
        mediaService.removeMediaFromHotel(hotelId, mediaId);
        return ResponseEntity.noContent().build();
    }
}