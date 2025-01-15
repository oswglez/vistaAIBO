package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // Create a new Media
    @PostMapping
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
    public ResponseEntity<List<Media>> getAllMedia() {
        List<Media> mediaList = mediaService.getAllMedia();
        return new ResponseEntity<>(mediaList, HttpStatus.OK);
    }

    // Get Media by ID
    @GetMapping("/{id}")
    public ResponseEntity<Media> getMediaById(@PathVariable Long id) {
        return mediaService.getMediaById(id)
                .map(media -> new ResponseEntity<>(media, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));
    }

    // Get all Media for a specific Room
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Media>> getAllMediaByRoomId(@PathVariable Long roomId) {
        List<Media> mediaList = mediaService.getAllMediaByRoomId(roomId);
        return new ResponseEntity<>(mediaList, HttpStatus.OK);
    }

    // Get all Media for a specific Hotel
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Media>> getAllMediaByHotelId(@PathVariable Long hotelId) {
        List<Media> mediaList = mediaService.getAllMediaByHotelId(hotelId);
        return new ResponseEntity<>(mediaList, HttpStatus.OK);
    }

    // Update Media
    @PutMapping("/{id}")
    public ResponseEntity<Media> updateMedia(@PathVariable Long id, @RequestBody Media mediaDetails) {
        Media updatedMedia = mediaService.updateMedia(id, mediaDetails);
        return new ResponseEntity<>(updatedMedia, HttpStatus.OK);
    }

    // Delete Media
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}