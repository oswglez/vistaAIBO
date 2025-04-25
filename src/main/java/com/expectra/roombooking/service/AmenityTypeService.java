package com.expectra.roombooking.service;

import com.expectra.roombooking.model.AmenityTypes;

import java.util.List;
import java.util.Optional;

public interface AmenityTypeService {
    // Operaciones básicas CRUD
    AmenityTypes createAmenityTypes(AmenityTypes amenity);
    Optional<AmenityTypes> getAmenityTypesById(Long id);
    List<AmenityTypes> getAllAmenityTypes();
    AmenityTypes updateAmenityTypes(Long id, AmenityTypes amenityDetails);
    void deleteAmenityTypes(Long id);
}