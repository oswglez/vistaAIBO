package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.service.MediaService;
import com.expectra.roombooking.service.HotelService;
import com.expectra.roombooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;
    private final HotelService hotelService;
    private final RoomService roomService;

    @Autowired
    public MediaController(MediaService mediaService, HotelService hotelService, RoomService roomService) {
        this.mediaService = mediaService;
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    // Create a new Media
    @PostMapping
    public ResponseEntity<Media> createMedia(@RequestParam(required = false) Long hotelId,
                                             @RequestParam(required = false) Long roomId,
                                             @RequestBody Media media) {
        if (hotelId == null && roomId == null) {
            throw new IllegalArgumentException("Either hotelId or roomId must be provided");
        }

        if (roomId != null) {
            roomService.getRoomById(roomId).orElseThrow(() ->
                    new ResourceNotFoundException("Room not found with id: " + roomId));
        } else {
            hotelService.getHotelById(hotelId).orElseThrow(() ->
                    new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        }

        Media createdMedia = mediaService.createMedia(media);
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
