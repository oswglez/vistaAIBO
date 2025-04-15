package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.AmenityType;
import com.expectra.roombooking.repository.AmenityTypeRepository;
import com.expectra.roombooking.service.AmenityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AmenityTypeServiceImpl implements AmenityTypeService {

    private final AmenityTypeRepository amenityTypeRepository;

    @Autowired
    public AmenityTypeServiceImpl(AmenityTypeRepository amenityTypeRepository) {
        this.amenityTypeRepository = amenityTypeRepository;
    }

    @Override
    @Transactional
    public AmenityType createAmenityType(AmenityType amenityType) {
        return amenityTypeRepository.save(amenityType);
    }

    @Override
    public Optional<AmenityType> getAmenityTypeById(Long id) {
        return amenityTypeRepository.findById(id);
    }

    @Override
    public List<AmenityType> getAllAmenitieType() {
        return (List<AmenityType>) amenityTypeRepository.findAll();
    }

    @Override
    @Transactional
    public AmenityType updateAmenityType(Long id, AmenityType amenityTypeDetails) {
        AmenityType amenityType = amenityTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AmenityType not found with id: " + id));

        amenityType.setAmenityTypeName(amenityTypeDetails.getAmenityTypeName());
        return amenityTypeRepository.save(amenityType);
    }

    @Override
    @Transactional
    public void deleteAmenityType(Long id) {
        AmenityType amenityType = amenityTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AmenityType not found with id: " + id));
        amenityTypeRepository.delete(amenityType);
    }
}