package com.expectra.roombooking.service;

import java.util.List;

import com.expectra.roombooking.model.MediaTypes;

import java.util.Optional;

public interface MediaTypesService {
    // Operaciones básicas CRUD
    MediaTypes createMediaTypes(MediaTypes mediaTypes);
    Optional<MediaTypes> getMediaTypesById(Long id);
    List<MediaTypes> getAllMediaTypes();
    MediaTypes updateMediaTypes(Long id, MediaTypes mediaTypesDetails);
    void deleteMediaTypes(Long id);
}