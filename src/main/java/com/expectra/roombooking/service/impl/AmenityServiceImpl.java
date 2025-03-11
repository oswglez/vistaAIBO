package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.AmenityRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.repository.RoomRepository;
import com.expectra.roombooking.service.AmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomServiceImpl roomServiceImpl;

    @Autowired
    public AmenityServiceImpl(AmenityRepository amenityRepository,
                              HotelRepository hotelRepository,
                              RoomRepository roomRepository, RoomServiceImpl roomServiceImpl) {
        this.amenityRepository = amenityRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.roomServiceImpl = roomServiceImpl;
    }

    @Override
    @Transactional
    public Amenity createAmenity(Long hotelId, Amenity amenity) {
        if (hotelId != null) {
            Hotel hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
            amenity.getHotels().add(hotel);
        }
        return amenityRepository.save(amenity);
    }

    @Override
    public Optional<Amenity> getAmenityById(Long id) {
        return amenityRepository.findById(id);
    }

    @Override
    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    @Override
    @Transactional
    public Amenity updateAmenity(Long id, Amenity amenityDetails) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));

        amenity.setAmenityCode(amenityDetails.getAmenityCode());
        amenity.setAmenityType(amenityDetails.getAmenityType());
        amenity.setAmenityDescription(amenityDetails.getAmenityDescription());

        return amenityRepository.save(amenity);
    }

    @Override
    @Transactional
    public void deleteAmenity(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + id));

        if (!amenity.getHotels().isEmpty() || !amenity.getRooms().isEmpty()) {
            throw new ResourceNotFoundException("amenity has actives relationships  id: " + id);
        }
        amenityRepository.delete(amenity);
    }

    @Override
    public List<Amenity> getAmenitiesByRoomId(Long roomId) {
        return amenityRepository.findAllAmenitiesByRoomId(roomId);
    }

    @Override
    public List<Amenity> getAmenitiesByHotelId(Long hotelId) {
        return amenityRepository.findAllAmenitiesByHotelId(hotelId);
    }

    @Override
    @Transactional
    public Amenity addAmenityToRoom(Long roomId, Long amenityId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + amenityId));

        amenity.getRooms().add(room);
        return amenityRepository.save(amenity);
    }

    @Override
    @Transactional
    public Amenity addAmenityToHotel(Long hotelId, Long amenityId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + amenityId));

        amenity.getHotels().add(hotel);
        return amenityRepository.save(amenity);
    }

    @Override
    @Transactional
    @EntityGraph(attributePaths = {"amenity"})
    public void removeAmenityFromRoom(Long roomId, Long amenityId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + roomId));
        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + amenityId));

        room.getAmenities().remove(amenity);
        roomRepository.save(room);
        roomRepository.flush();

        if (amenity.getHotels().isEmpty() && amenity.getRooms().isEmpty()) {
            amenityRepository.delete(amenity);
        }
    }

    @Override
    @Transactional
    @EntityGraph(attributePaths = {"amenity"})
    public void removeAmenityFromHotel(Long hotelId, Long amenityId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with id: " + amenityId));

        hotel.getAmenities().remove(amenity);
        hotelRepository.save(hotel);
        hotelRepository.flush();


        // 🔹 Verificar si el Amenity quedó huérfano y eliminarlo
        if (amenity.getHotels().isEmpty() && amenity.getRooms().isEmpty()) {
            amenityRepository.delete(amenity);
        }


//
//        if (amenity.getHotels().isEmpty() && amenity.getRooms().isEmpty()) {
//            amenityRepository.delete(amenity);
//        }
    }
}