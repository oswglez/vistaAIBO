package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Media;
import java.util.List;
import java.util.Optional;

public interface MediaService {
    Media createMedia(Media media, Long hotelId, Long roomId);
    List<Media> getAllMedia();
    Optional<Media> getMediaById(Long id);
    Media updateMedia(Long id, Media mediaDetails);
    void deleteMedia(Long id);
    void removeMediaFromRoom(Long roomId, Long mediaId);
    void removeMediaFromHotel(Long hotelId, Long mediaId);
}