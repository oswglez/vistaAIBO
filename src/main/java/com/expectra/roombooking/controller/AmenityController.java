package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.service.AmenityService;
import com.expectra.roombooking.service.HotelService;
import com.expectra.roombooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;
    private final HotelService hotelService;
    private final RoomService roomService;

    @Autowired
    public AmenityController(AmenityService amenityService, HotelService hotelService, RoomService roomService) {
        this.amenityService = amenityService;
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    // Create a new Amenity
    @PostMapping
    public ResponseEntity<Amenity> createAmenity(@RequestParam(required = false) Long hotelId,
                                                 @RequestParam(required = false) Long roomId,
                                                 @RequestBody Amenity amenity) {
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

        Amenity createdAmenity = amenityService.createAmenity(amenity);
        return new ResponseEntity<>(createdAmenity, HttpStatus.CREATED);
    }

    // Get all Amenities
    @GetMapping
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        List<Amenity> amenities = amenityService.getAllAmenities();
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }

    // Get Amenity by ID
    @GetMapping("/{id}")
    public ResponseEntity<Amenity> getAmenityById(@PathVariable Long id) {
        return amenityService.getAmenityById(id)
                .map(amenity -> new ResponseEntity<>(amenity, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));
    }

    // Update Amenity
    @PutMapping("/{id}")
    public ResponseEntity<Amenity> updateAmenity(@PathVariable Long id, @RequestBody Amenity amenityDetails) {
        Amenity updatedAmenity = amenityService.updateAmenity(id, amenityDetails);
        return new ResponseEntity<>(updatedAmenity, HttpStatus.OK);
    }

    // Delete Amenity
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
