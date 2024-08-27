package com.expectra.roombooking.service;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Photo;
import com.expectra.roombooking.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo createPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Optional<Photo> getPhotoById(Long id) {
        return photoRepository.findById(id);
    }

    public Photo updatePhoto(Long id, Photo photoDetails) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));

        photo.setCode(photoDetails.getCode());
        photo.setPhoto(photoDetails.getPhoto());

        return photoRepository.save(photo);
    }

    public void deletePhoto(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));
        photoRepository.delete(photo);
    }
}


