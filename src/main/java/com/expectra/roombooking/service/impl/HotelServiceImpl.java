package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.dto.HotelDTO;
import com.expectra.roombooking.dto.RoomDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.*;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.service.HotelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository, ModelMapper modelMapper) {
        this.hotelRepository = hotelRepository;
        this.modelMapper =  modelMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> findHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    @Override
    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public void deleteHotelById(Long hotelId) {
        hotelRepository.deleteById(hotelId);
    }
    @Override
    public void logicalDeleteHotel(Long hotelId) {
        hotelRepository.markHotelAsDeleted(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findHotelsByName(String hotelName) {
        return hotelRepository.findByHotelName(hotelName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Amenity> findHotelAmenities(Long hotelId) {
        return hotelRepository.findAllAmenitiesByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Media> findHotelMedias(Long hotelId) {
        return hotelRepository.findAllMediasByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contact> findAllContactsByHotelId(Long hotelId) {
        return hotelRepository.findAllContactsByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findAllAddressesByHotelId(Long hotelId) {
        return hotelRepository.findAllAddressesByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> findHotelRooms(Long hotelId) {
        return hotelRepository.findAllRoomsByHotelId(hotelId);
    }
    @Override
    @Transactional(readOnly = true)
    public HotelDTO getHotelAndRoomsByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.getHotelAndRoomsByHotelId(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado"));
        HotelDTO dto = modelMapper.map(hotel, HotelDTO.class);

        dto.setRooms(hotel.getRooms().stream()
                .map(room -> {
                    return modelMapper.map(room, RoomDTO.class);
                })
                .collect(Collectors.toSet()));
        return dto;
    }
    @Override
    @Transactional(readOnly = true)
    public HotelDTO findHotelAndRoomsByHotelIdAndRoomType(Long hotelId, String roomType) {
        Hotel hotel = hotelRepository.findHotelAndRoomsByHotelIdAndRoomType(hotelId, roomType)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado"));
        HotelDTO dto = modelMapper.map(hotel, HotelDTO.class);

        dto.setRooms(hotel.getRooms().stream()
                .map(room -> {
                    return modelMapper.map(room, RoomDTO.class);
                })
                .collect(Collectors.toSet()));
        return dto;
    }
}