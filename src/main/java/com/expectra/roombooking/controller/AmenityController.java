package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.service.AmenityService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Crear una amenity", description = "Crea una amenity.")
    public ResponseEntity<Amenity> createAmenity(
            @RequestParam(required = false) Long hotelId,
            @RequestBody Amenity amenity) {
        Amenity createdAmenity = amenityService.createAmenity(hotelId, amenity);
        return new ResponseEntity<>(createdAmenity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtiene todas las amenity", description = "Recupera todas las amenities de la base de datos.")
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        List<Amenity> amenities = amenityService.getAllAmenities();
        return new ResponseEntity<>(amenities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una amenity", description = "Recupera una amenity usando su ID.")
    public ResponseEntity<Amenity> getAmenityById(@PathVariable Long id) {
        return amenityService.getAmenityById(id)
                .map(amenity -> new ResponseEntity<>(amenity, HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));
    }

//    @GetMapping("/room/{roomId}")
//    @Operation(summary = "Amenities de una habitacion", description = "Obtiene todas las amenities de una habitacion especificada")
//    public ResponseEntity<List<Amenity>> getAmenitiesByRoomId(@PathVariable Long roomId) {
//        List<Amenity> amenities = amenityService.getAmenitiesByRoomId(roomId);
//        return new ResponseEntity<>(amenities, HttpStatus.OK);
//    }
//
//    @GetMapping("/hotel/{hotelId}")
//    @Operation(summary = "Amenities de un hotel", description = "Obtiene todas las amenities de un hotel especificado")
//    public ResponseEntity<List<Amenity>> getAmenitiesByHotelId(@PathVariable Long hotelId) {
//        List<Amenity> amenities = amenityService.getAmenitiesByHotelId(hotelId);
//        return new ResponseEntity<>(amenities, HttpStatus.OK);
//    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una amenity", description = "Actualiza los datos de una amenity existente usando su Id.")
    public ResponseEntity<Amenity> updateAmenity(@PathVariable Long id, @RequestBody Amenity amenityDetails) {
        Amenity updatedAmenity = amenityService.updateAmenity(id, amenityDetails);
        return new ResponseEntity<>(updatedAmenity, HttpStatus.OK);
    }
//
//    @PostMapping("/room/{roomId}/amenity/{amenityId}")
//    @Operation(summary = "Actualiza una amenity", description = "Actualiza los datos de una amenity existente usando su Id y la habitación Id.")
//    public ResponseEntity<Amenity> addAmenityToRoom(
//            @PathVariable Long roomId,
//            @PathVariable Long amenityId) {
//        Amenity amenity = amenityService.addAmenityToRoom(roomId, amenityId);
//        return new ResponseEntity<>(amenity, HttpStatus.OK);
//    }
//
//    @PostMapping("/hotel/{hotelId}/amenity/{amenityId}")
//    @Operation(summary = "Actualiza una amenity", description = "Actualiza los datos de una amenity existente usando su Id y el hotel Id.")
//    public ResponseEntity<Amenity> addAmenityToHotel(
//            @PathVariable Long hotelId,
//            @PathVariable Long amenityId) {
//        Amenity amenity = amenityService.addAmenityToHotel(hotelId, amenityId);
//        return new ResponseEntity<>(amenity, HttpStatus.OK);
//    }

    @DeleteMapping("/room/{roomId}/amenity/{amenityId}")
    @Operation(summary = "Elimina una amenity", description = "Elimina o desconecta una amenity de una habitación.")
    public ResponseEntity<Void> removeAmenityFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long amenityId) {
        amenityService.removeAmenityFromRoom(roomId, amenityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/hotel/{hotelId}/amenity/{amenityId}")
    @Operation(summary = "Elimina una amenity", description = "Elimina o desconecta una amenity de un hotel.")
    public ResponseEntity<Void> removeAmenityFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long amenityId) {
        amenityService.removeAmenityFromHotel(hotelId, amenityId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una amenity", description = "Elimina una amenity.")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long id) {
        amenityService.deleteAmenity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}