package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.HotelListDTO;
import com.expectra.roombooking.model.AmenityTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AmenityTypeService {
    // Operaciones básicas CRUD
    AmenityTypes createAmenityTypes(AmenityTypes amenity);
    Optional<AmenityTypes> getAmenityTypesById(Long id);
 //   List<AmenityTypes> getAllAmenityTypes();

    Page<AmenityTypes> getAllAmenitiesTypes(Pageable pageable);
    AmenityTypes updateAmenityTypes(Long id, AmenityTypes amenityDetails);
    void deleteAmenityTypes(Long id);
}