package com.expectra.roombooking.service;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.repository.MediaRepository;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;

    @Autowired
    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Media createMedia(Media media) {
        return mediaRepository.save(media);
    }

    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    public Optional<Media> getMediaById(Long id) {
        return mediaRepository.findById(id);
    }

    public Media updateMedia(Long id, Media mediaDetails) {
        Media media= mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));

        media.setMediaType(mediaDetails.getMediaType());
        media.setMediaUrl(mediaDetails.getMediaUrl());
        media.setMediaCode(mediaDetails.getMediaCode());
        media.setMediaDescription(mediaDetails.getMediaDescription());

        return mediaRepository.save(media);
    }

    public void deleteMedia(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));
        mediaRepository.delete(media);
    }
}


