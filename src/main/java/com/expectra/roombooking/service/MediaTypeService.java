package com.expectra.roombooking.service;

import com.expectra.roombooking.model.AmenityType;
import com.expectra.roombooking.model.MediaType;

import java.util.List;
import java.util.Optional;

public interface MediaTypeService {
    // Operaciones básicas CRUD
    MediaType createMediaType(MediaType amenity);
    Optional<MediaType> getMediaTypeById(Long id);
    List<MediaType> getAllMediaType();
    MediaType updateMediaType(Long id, MediaType mediaDetails);
    void deleteMediaType(Long id);
}