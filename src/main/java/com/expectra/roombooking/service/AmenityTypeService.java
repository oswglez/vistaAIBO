package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.AmenityType;

import java.util.List;
import java.util.Optional;

public interface AmenityTypeService {
    // Operaciones básicas CRUD
    AmenityType createAmenityType(AmenityType amenity);
    Optional<AmenityType> getAmenityTypeById(Long id);
    List<AmenityType> getAllAmenitieType();
    AmenityType updateAmenityType(Long id, AmenityType amenityDetails);
    void deleteAmenityType(Long id);
}