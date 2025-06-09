package com.expectra.roombooking.service;

import java.util.List;

import com.expectra.roombooking.model.AmenityTypes;
import com.expectra.roombooking.model.MediaTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MediaTypesService {
    // Operaciones básicas CRUD
    MediaTypes createMediaTypes(MediaTypes mediaTypes);
    Optional<MediaTypes> getMediaTypesById(Long id);
    Page<MediaTypes> getAllMediaTypes(Pageable pageable);
    MediaTypes updateMediaTypes(Long id, MediaTypes mediaTypesDetails);
    void deleteMediaTypes(Long id);
}