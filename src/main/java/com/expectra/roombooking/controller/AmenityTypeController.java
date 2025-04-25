package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.AmenityTypes;
import com.expectra.roombooking.service.AmenityService;
import com.expectra.roombooking.service.AmenityTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/amenityType")
@Tag(name = "Amenity Management", description = "Endpoints para gestión de amenities para hoteles y habitaciones")

@CrossOrigin(origins = "*")
public class AmenityTypeController {

    private final AmenityTypeService amenityTypeService;
    private final String messageNotFound = "Amenity not found with ID: ";

    @Autowired
    public AmenityTypeController(final AmenityTypeService amenityTypeService) {
        this.amenityTypeService = amenityTypeService;
    }

@PostMapping
public ResponseEntity<AmenityTypes> createAmenityType(@RequestBody Map<String, Object> requestBody) {
    AmenityTypes amenityTypes = new AmenityTypes();
    amenityTypes.setAmenityTypeName((String) requestBody.get("amenityTypeName"));

    AmenityTypes createdAmenityTypes = amenityTypeService.createAmenityTypes(amenityTypes);
    return ResponseEntity.ok(createdAmenityTypes);
}

@GetMapping("/{amenityTypeId}")
@Operation(summary = "Obtiene una amenityType", description = "Recupera una amenityType usando su ID.")
public ResponseEntity<AmenityTypes> getAmenityTypeByIdById(@PathVariable Long amenityTypeId) {
    return amenityTypeService.getAmenityTypesById(amenityTypeId)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + amenityTypeId));
}

@GetMapping
@Operation(summary = "Obtiene todas las amenity", description = "Recupera todas las amenities de la base de datos.")
public ResponseEntity<List<AmenityTypes>> getAllAmenities() {
    List<AmenityTypes> amenitiyTypes = amenityTypeService.getAllAmenityTypes();
    return ResponseEntity.ok(amenitiyTypes);
}

@PutMapping("/{amenityTypeId}")
@Operation(summary = "Actualiza una amenityType", description = "Actualiza los datos de una amenity Type existente usando su Id.")
public ResponseEntity<AmenityTypes> updateAmenity(@PathVariable Long amenityTypeId, @RequestBody AmenityTypes amenityTypeDetails) {
    AmenityTypes updatedAmenityTypes = amenityTypeService.updateAmenityTypes(amenityTypeId, amenityTypeDetails);
    return ResponseEntity.ok(updatedAmenityTypes);
}

@DeleteMapping("/{amenityTypeId}")
@Operation(summary = "" +
        "Elimina una amenityType", description = "Elimina una amenity Type existente usando su Id.")
public ResponseEntity<AmenityTypes> deleteAmenityType(@PathVariable Long amenityTypeId) {
    amenityTypeService.deleteAmenityTypes(amenityTypeId);
    return ResponseEntity.noContent().build();
}

//    @DeleteMapping("/{amenityId}")
//    @Operation(summary = "Elimina una amenity", description = "Elimina una amenity.")
//    public ResponseEntity<Void> deleteAmenity(@PathVariable Long amenityId) {
//        amenityTypeService.deleteAmenity(amenityId);
//        return ResponseEntity.noContent().build();
//    }
}