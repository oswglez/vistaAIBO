package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@Tag(name = "Hotel Management", description = "Endpoints para gestión de hoteles")

@CrossOrigin(origins = "*")
public class HotelController {
    private final String messageNotfound = "Hotel not found" ;
    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // Create a new Hotel
    @PostMapping
    @Operation(summary = "Actualiza un hotel", description = "Crea un hotel usando el hotelId.")
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        Hotel savedHotel = hotelService.saveHotel(hotel);
        return new ResponseEntity<>(savedHotel, HttpStatus.CREATED);
    }

    // Get all Hotels
    @GetMapping
    @Operation(summary = "Consulta todos los hoteles", description = "Consulta de todos los hoteles.")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.findAllHotels();
        return ResponseEntity.ok(hotels);
    }

    // Get Hotel by ID
    @GetMapping("/{id}")
    @Operation(summary = "Consulta un hotel", description = "Consulta un hotel usando el hotelId.")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        return hotelService.findHotelById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotfound + id));
    }

    // Update Hotel
    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un hotel", description = "Actualiza un hotel usando el hotelId.")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotelDetails) {
        return hotelService.findHotelById(id)
                .map(existingHotel -> {
                    // Mantener el ID original
                    hotelDetails.setHotelId(id);
                    Hotel updated = hotelService.saveHotel(hotelDetails);
                    return ResponseEntity.ok(updated);
                })
                .orElseThrow(() -> new ResourceNotFoundException(messageNotfound + id));
    }

    // Delete Hotel
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un hotel", description = "Elimmina y sus habitaciones un hotel usando el hotelId.")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        if (!hotelService.findHotelById(id).isPresent()) {
            throw new ResourceNotFoundException(messageNotfound + id);
        }
        hotelService.deleteHotelById(id);
        return ResponseEntity.noContent().build();
    }

    // Get Hotels by Name
    @GetMapping("/search")
    @Operation(summary = "Consulta de hoteles", description = "Consulta un hotel por su nombre.")
    public ResponseEntity<List<Hotel>> getHotelsByName(@RequestParam String name) {
        List<Hotel> hotels = hotelService.findHotelsByName(name);
        return ResponseEntity.ok(hotels);
    }

    // Get Hotel Amenities
    @GetMapping("/{id}/amenities")
    @Operation(summary = "Consulta las amenities", description = "Consulta las amenities de un hotel usando el hotelId.")
    public ResponseEntity<List<Amenity>> getHotelAmenities(@PathVariable Long id) {
        if (!hotelService.findHotelById(id).isPresent()) {
            throw new ResourceNotFoundException(messageNotfound + id);
        }
        List<Amenity> amenities = hotelService.findHotelAmenities(id);
        return ResponseEntity.ok(amenities);
    }

    // Get Hotel Media
    @GetMapping("/{id}/media")
    @Operation(summary = "Consulta las medias", description = "Consulta las medias de un de un hotel usando el hotelId.")
    public ResponseEntity<List<Media>> getHotelMedia(@PathVariable Long id) {
        if (!hotelService.findHotelById(id).isPresent()) {
            throw new ResourceNotFoundException(messageNotfound + id);
        }
        List<Media> media = hotelService.findHotelMedias(id);
        return ResponseEntity.ok(media);
    }
}