package com.expectra.roombooking.service;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.repository.AmenityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;

    @Autowired
    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public Amenity createAmenity(Amenity amenity) {
        return amenityRepository.save(amenity);
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    public Optional<Amenity> getAmenityById(Long id) {
        return amenityRepository.findById(id);
    }

    public Amenity updateAmenity(Long id, Amenity amenityDetails) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found"));

        amenity.setCode(amenityDetails.getCode());
        amenity.setDescription(amenityDetails.getDescription());

        return amenityRepository.save(amenity);
    }

    public void deleteAmenity(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found"));
        amenityRepository.delete(amenity);
    }
}
