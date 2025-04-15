package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.MediaType;
import com.expectra.roombooking.repository.MediaTypeRepository;
import com.expectra.roombooking.service.MediaTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MediaTypeServiceImpl implements MediaTypeService {

    private final MediaTypeRepository mediaTypeRepository;

    @Autowired
    public MediaTypeServiceImpl(MediaTypeRepository mediaTypeRepository) {
        this.mediaTypeRepository = mediaTypeRepository;
    }

    @Override
    @Transactional
    public MediaType createMediaType(MediaType mediaType) {
        return mediaTypeRepository.save(mediaType);
    }

    @Override
    public Optional<MediaType> getMediaTypeById(Long id) {
        return mediaTypeRepository.findById(id);
    }

    @Override
    public List<MediaType> getAllMediaType() {
        return (List<MediaType>) mediaTypeRepository.findAll();
    }

    @Override
    @Transactional
    public MediaType updateMediaType(Long id, MediaType mediaTypeDetails) {
        MediaType mediaType = mediaTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaType not found with id: " + id));

        mediaType.setMediaTypeName(mediaTypeDetails.getMediaTypeName());
        return mediaTypeRepository.save(mediaType);
    }

    @Override
    @Transactional
    public void deleteMediaType(Long id) {
        MediaType mediaType = mediaTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaType not found with id: " + id));
        mediaTypeRepository.delete(mediaType);
    }
}