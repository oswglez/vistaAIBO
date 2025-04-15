package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.AmenityDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.AmenityType;
import com.expectra.roombooking.service.AmenityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/amenities")
@Tag(name = "Amenity Management", description = "Endpoints para gestión de amenities para hoteles y habitaciones")

@CrossOrigin(origins = "*")
public class AmenityController {

    private final AmenityService amenityService;
    private final String messageNotFound = "Amenity not found with ID: ";

    @Autowired
    public AmenityController(final AmenityService amenityService) {
        this.amenityService = amenityService;
    }

//. falta perfeccionar el metodo para que verifique si la amenity existe en ese caso solo lo conecta

//    @PostMapping
//    @Operation(summary = "Crear una amenity", description = "Crea una amenity.")
//    public ResponseEntity<Amenity> createAmenity(
//            @RequestParam(required = false) Long hotelId,
//            @RequestBody Amenity amenity) {
//        Amenity createdAmenity = amenityService.createAmenity(hotelId, amenity);
//        return new ResponseEntity<>(createdAmenity, HttpStatus.CREATED);
//    }
@PostMapping
public ResponseEntity<Amenity> createAmenity(@RequestBody Map<String, Object> requestBody) {
    Amenity amenity = new Amenity();
    amenity.setAmenityCode((Integer) requestBody.get("amenityCode"));
    amenity.setAmenityDescription((String) requestBody.get("amenityDescription"));

    // Conversión de String a enum
    String amenityTypeStr = (String) requestBody.get("amenityType");
    amenity.setAmenityType(AmenityType.valueOf(amenityTypeStr));

    // Si hay un hotelId en el request
    Long hotelId = requestBody.get("hotelId") != null ?
            Long.valueOf(requestBody.get("hotelId").toString()) : null;

    Amenity createdAmenity = amenityService.createAmenity(hotelId, amenity);
    return ResponseEntity.ok(createdAmenity);
}
    @GetMapping
    @Operation(summary = "Obtiene todas las amenity", description = "Recupera todas las amenities de la base de datos.")
    public ResponseEntity<List<Amenity>> getAllAmenities() {
        List<Amenity> amenities = amenityService.getAllAmenities();
        return ResponseEntity.ok(amenities);
    }

    @GetMapping("/{amenityId}")
    @Operation(summary = "Obtiene una amenity", description = "Recupera una amenity usando su ID.")
    public ResponseEntity<Amenity> getAmenityById(@PathVariable Long amenityId) {
        return amenityService.getAmenityById(amenityId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + amenityId));
    }

    @PutMapping("/{amenityId}")
    @Operation(summary = "Actualiza una amenity", description = "Actualiza los datos de una amenity existente usando su Id.")
    public ResponseEntity<Amenity> updateAmenity(@PathVariable Long amenityId, @RequestBody Amenity amenityDetails) {
        Amenity updatedAmenity = amenityService.updateAmenity(amenityId, amenityDetails);
        return ResponseEntity.ok(updatedAmenity);
    }

    @DeleteMapping("/rooms/{roomId}/amenities/{amenityId}")
    @Operation(summary = "Desconecta o elimina una amenity de una habitacion", description = "Elimina o desconecta una amenity de una habitación.")
    public ResponseEntity<Void> removeAmenityFromRoom(
            @PathVariable Long roomId,
            @PathVariable Long amenityId) {
        amenityService.removeAmenityFromRoom(roomId, amenityId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hotels/{hotelId}/amenities/{amenityId}")
    @Operation(summary = "Desconecta o elimina una amenity de. un. hotel", description = "Elimina o desconecta una amenity de un hotel.")
    public ResponseEntity<Void> removeAmenityFromHotel(
            @PathVariable Long hotelId,
            @PathVariable Long amenityId) {
        amenityService.removeAmenityFromHotel(hotelId, amenityId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{amenityId}")
    @Operation(summary = "Elimina una amenity", description = "Elimina una amenity.")
    public ResponseEntity<Void> deleteAmenity(@PathVariable Long amenityId) {
        amenityService.deleteAmenity(amenityId);
        return ResponseEntity.noContent().build();
    }
}