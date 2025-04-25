package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.AmenityTypes;
import com.expectra.roombooking.repository.AmenityTypeRepository;
import com.expectra.roombooking.service.AmenityTypeService;
import com.expectra.roombooking.service.AmenityTypeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AmenityTypeServiceImpl implements AmenityTypeService {

    private final AmenityTypeRepository amenityTypeRepository;
    private final AmenityTypeValidationService amenityTypeValidator;



    @Autowired
    public AmenityTypeServiceImpl(AmenityTypeRepository amenityTypeRepository, AmenityTypeValidationService amenityTypeValidator) {
        this.amenityTypeRepository = amenityTypeRepository;
        this.amenityTypeValidator = amenityTypeValidator;
    }

    @Override
    @Transactional
    public AmenityTypes createAmenityTypes(AmenityTypes amenityTypes) {
     //   amenityTypeValidator.validateType(amenityTypes.getAmenityTypeName());

        return amenityTypeRepository.save(amenityTypes);
    }

    @Override
    public Optional<AmenityTypes> getAmenityTypesById(Long id) {
        return amenityTypeRepository.findById(id);
    }

    @Override
    public List<AmenityTypes> getAllAmenityTypes() {
        return (List<AmenityTypes>) amenityTypeRepository.findAll();
    }

    @Override
    @Transactional
    public AmenityTypes updateAmenityTypes(Long id, AmenityTypes amenityTypesDetails) {
        amenityTypeValidator.validateType(amenityTypesDetails.getAmenityTypeName());

        AmenityTypes amenityTypes = amenityTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AmenityType not found with id: " + id));

        amenityTypes.setAmenityTypeName(amenityTypesDetails.getAmenityTypeName());
        return amenityTypeRepository.save(amenityTypes);
    }

    @Override
    @Transactional
    public void deleteAmenityTypes(Long id) {
        AmenityTypes amenityTypes = amenityTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AmenityType not found with id: " + id));
        amenityTypeRepository.delete(amenityTypes);
    }
}