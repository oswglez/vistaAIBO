// MediaServiceImpl.java
package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.MediaRepository;
import com.expectra.roombooking.repository.RoomRepository;
import com.expectra.roombooking.service.AddressService;
import com.expectra.roombooking.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final MediaRepository mediaRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public AddressServiceImpl(MediaRepository mediaRepository,
                              HotelRepository hotelRepository,
                              RoomRepository roomRepository) {
        this.mediaRepository = mediaRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Hotel> findAllHotels() {
        return null;
    }

    @Override
    public Optional<Hotel> findHotelById(Long hotelId) {
        return Optional.empty();
    }

    @Override
    public Hotel saveHotel(Hotel hotel) {
        return null;
    }

    @Override
    public void deleteHotel(Hotel hotel) {

    }

    @Override
    public void deleteHotelById(Long hotelId) {

    }

    @Override
    public List<Hotel> findHotelsByName(String hotelName) {
        return null;
    }

    @Override
    public List<Amenity> findHotelAmenities(Long hotelId) {
        return null;
    }

    @Override
    public List<Media> findHotelMedias(Long hotelId) {
        return null;
    }

    @Override
    public List<Room> findHotelRooms(Long hotelId) {
        return null;
    }
}