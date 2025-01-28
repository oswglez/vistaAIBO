package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.service.AmenityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amenities")
@Tag(name = "Amenity Management", description = "Endpoints para gestión de amenities para hoteles y habitaciones")
public class AmenityController {

    private final AmenityService amenityService;

    @Autowired
    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @PostMapping
    public ResponseEntity<Amenity> createAmenity(
            @RequestParam(required = false) Long hotelId,
            @RequestBody Amenity amenity) {
        Amenity createdAmenity = amenityService.createAmenity(hotelId, amenity);
        return new ResponseEntity<>(createdAmenity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        List<Amenity> amenities = amenityService.getAllAmenities();
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Amenity> getAmenityById(@PathVariable Long id) {
        return amenityService.getAmenityById(id)
                .map(amenity -> new ResponseEntity<>(amenity, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Amenity>> getAmenitiesByRoomId(@PathVariable Long roomId) {
        List<Amenity> amenities = amenityService.getAmenitiesByRoomId(roomId);
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Amenity>> getAmenitiesByHotelId(@PathVariable Long hotelId) {
        List<Amenity> amenities = amenityService.getAmenitiesByHotelId(hotelId);
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Amenity> updateAmenity(@PathVariable Long id, @RequestBody Amenity amenityDetails) {
        Amenity updatedAmenity = amenityService.updateAmenity(id, amenityDetails);
        return new ResponseEntity<>(updatedAmenity, HttpStatus.OK);
    }

    @PostMapping("/room/{roomId}/amenity/{amenityId}")
    public ResponseEntity<Amenity> addAmenityToRoom(
            @PathVariable Long roomId,
            @PathVariable Long amenityId) {
        Amenity amenity = amenityService.addAmenityToRoom(roomId, amenityId);
        return new ResponseEntity<>(amenity, HttpStatus.OK);
    }

    @PostMapping("/hotel/{hotelId}/amenity/{amenityId}")
    public ResponseEntity<Amenity> addAmenityToHotel(
            @PathVariable Long hotelId,
            @PathVariable Long amenityId) {
        Amenity amenity = amenityService.addAmenityToHotel(hotelId, amenityId);
        return new ResponseEntity<>(amenity, HttpStatus.OK);
    }

    @DeleteMapping("/room/{roomId}/amenity/{amenityId}")
    public ResponseEntity<Void> removeAmenityFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long amenityId) {
        amenityService.removeAmenityFromRoom(roomId, amenityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/hotel/{hotelId}/amenity/{amenityId}")
    public ResponseEntity<Void> removeAmenityFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long amenityId) {
        amenityService.removeAmenityFromHotel(hotelId, amenityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}