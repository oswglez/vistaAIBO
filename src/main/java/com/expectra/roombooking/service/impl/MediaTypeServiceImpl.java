package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.AmenityTypes;
import com.expectra.roombooking.model.MediaTypes;
import com.expectra.roombooking.repository.MediaTypesRepository;
import com.expectra.roombooking.service.MediaTypesService;
import com.expectra.roombooking.service.MediaTypesValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MediaTypeServiceImpl implements MediaTypesService {

    private final MediaTypesRepository mediaTypesRepository;
    private final MediaTypesValidationService mediaTypesValidator;

    @Autowired
    public MediaTypeServiceImpl(MediaTypesRepository mediaTypesRepository, MediaTypesValidationService mediaTypesValidator) {
        this.mediaTypesRepository = mediaTypesRepository;
        this.mediaTypesValidator = mediaTypesValidator;
    }

    @Override
    @Transactional
    public MediaTypes createMediaTypes(MediaTypes mediaTypes) {
        return mediaTypesRepository.save(mediaTypes);
    }

    @Override
    public Optional<MediaTypes> getMediaTypesById(Long id) {
        return mediaTypesRepository.findById(id);
    }

    @Override
    public Page<MediaTypes> getAllMediaTypes(Pageable pageable) {
        return mediaTypesRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public MediaTypes updateMediaTypes(Long id, MediaTypes mediaTypeDetails) {
        mediaTypesValidator.validateType(mediaTypeDetails.getMediaTypeName());

        MediaTypes mediaType = mediaTypesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaType not found with id: " + id));

        mediaType.setMediaTypeDescription(mediaTypeDetails.getMediaTypeDescription());
        mediaType.setMediaTypeName(mediaTypeDetails.getMediaTypeName());
        return mediaTypesRepository.save(mediaType);
    }

    @Override
    @Transactional
    public void deleteMediaTypes(Long id) {
        MediaTypes mediaTypes = mediaTypesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaType not found with id: " + id));
        mediaTypesRepository.delete(mediaTypes);
    }
}