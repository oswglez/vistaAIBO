// MediaServiceImpl.java
package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.MediaRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.RoomRepository;
import com.expectra.roombooking.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public MediaServiceImpl(MediaRepository mediaRepository,
                            HotelRepository hotelRepository,
                            RoomRepository roomRepository) {
        this.mediaRepository = mediaRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public Media createMedia(Media media, Long hotelId, Long roomId) {
        Media savedMedia = mediaRepository.save(media);

        if (hotelId != null) {
            Hotel hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
            hotel.getMedia().add(savedMedia);
            hotelRepository.save(hotel);
        }

        if (roomId != null) {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
            room.getMedia().add(savedMedia);
            roomRepository.save(room);
        }

        return savedMedia;
    }

    @Override
    public List<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    @Override
    public Optional<Media> getMediaById(Long id) {
        return mediaRepository.findById(id);
    }

    @Override
    @Transactional
    public Media updateMedia(Long id, Media mediaDetails) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));

        media.setMediaCode(mediaDetails.getMediaCode());
        media.setMediaType(mediaDetails.getMediaType());
        media.setMediaDescription(mediaDetails.getMediaDescription());
        media.setMediaUrl(mediaDetails.getMediaUrl());

        return mediaRepository.save(media);
    }

    @Override
    @Transactional
    public void deleteMedia(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found with id: " + id));

        // Eliminar las referencias en hoteles
        for (Hotel hotel : media.getHotels()) {
            hotel.getMedia().remove(media);
            hotelRepository.save(hotel);
        }

        // Eliminar las referencias en habitaciones
        for (Room room : media.getRooms()) {
            room.getMedia().remove(media);
            roomRepository.save(room);
        }

        mediaRepository.delete(media);
    }

    @Override
    @Transactional
    public void removeMediaFromRoom(Long roomId, Long mediaId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + mediaId));

        media.getRooms().remove(room);
        mediaRepository.save(media);
    }

    @Override
    @Transactional
    public void removeMediaFromHotel(Long hotelId, Long mediaId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + mediaId));

        media.getHotels().remove(hotel);
        mediaRepository.save(media);
    }
}