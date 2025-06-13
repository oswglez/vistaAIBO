package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.AmenityTypes;
import com.expectra.roombooking.service.AmenityTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/amenityType")
@Tag(name = "Amenity Management", description = "Endpoints for managing hotel and room amenities")

@CrossOrigin(origins = "*")
public class AmenityTypeController {

    private final AmenityTypeService amenityTypeService;
    private final String messageNotFound = "Amenity not found with ID: ";

    @Autowired
    public AmenityTypeController(final AmenityTypeService amenityTypeService) {
        this.amenityTypeService = amenityTypeService;
    }

    @PostMapping
    @Operation(summary = "Create an amenity type", description = "Creates an amenity type using its ID.")
    public ResponseEntity<AmenityTypes> createAmenityType(@RequestBody Map<String, Object> requestBody) {
        AmenityTypes amenityTypes = new AmenityTypes();
        amenityTypes.setAmenityTypeName((String) requestBody.get("amenityTypeName"));
        amenityTypes.setAmenityTypeDescription((String) requestBody.get("amenityTypeDescription"));

        AmenityTypes createdAmenityTypes = amenityTypeService.createAmenityTypes(amenityTypes);
        return ResponseEntity.ok(createdAmenityTypes);
    }

    @GetMapping("/{amenityTypeId}")
    @Operation(summary = "Get an amenity type", description = "Retrieves an amenity type using its ID.")
    public ResponseEntity<AmenityTypes> getAmenityTypeByIdById(@PathVariable Long amenityTypeId) {
        return amenityTypeService.getAmenityTypesById(amenityTypeId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + amenityTypeId));
    }

    @GetMapping
    @Operation(summary = "Get all amenity types", description = "Retrieves all amenity types from the database.")
    public ResponseEntity<Page<AmenityTypes>> getAllAmenities(
            @PageableDefault(page = 0, size = 10) // Default pagination values (0-indexed page)
            @SortDefault(sort = "amenityTypeName", direction = Sort.Direction.ASC) // Ordenamiento por defecto
            Pageable pageable) {
        Page<AmenityTypes> amenitiyTypes = amenityTypeService.getAllAmenitiesTypes(pageable);
        return ResponseEntity.ok(amenitiyTypes);
    }

    @PutMapping("/{amenityTypeId}")
    @Operation(summary = "Update an amenity type", description = "Updates an existing amenity type using its ID.")
    public ResponseEntity<AmenityTypes> updateAmenity(@PathVariable Long amenityTypeId, @RequestBody AmenityTypes amenityTypeDetails) {
        AmenityTypes updatedAmenityTypes = amenityTypeService.updateAmenityTypes(amenityTypeId, amenityTypeDetails);
        return ResponseEntity.ok(updatedAmenityTypes);
    }

    @DeleteMapping("/{amenityTypeId}")
    @Operation(summary = "Delete an amenity type", description = "Deletes an existing amenity type using its ID.")
    public ResponseEntity<AmenityTypes> deleteAmenityType(@PathVariable Long amenityTypeId) {
        amenityTypeService.deleteAmenityTypes(amenityTypeId);
        return ResponseEntity.noContent().build();
    }
}