package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.RoomRepository;
import com.expectra.roombooking.service.RoomService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public void deleteRoomById(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    @Override
    public Page<Room> getAllRooms(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }

    @Override
    public Page<Room> getRoomsByHotelId(Long hotelId, Pageable pageable) {
        return roomRepository.findByHotelHotelId(hotelId, pageable);
    }

    @Override
    public List<Media> getAllMediasByHotelIdAndRoomId(Long hotelId, Long roomId) {
        return roomRepository.getAllMediasByHotelIdAndRoomId(hotelId, roomId);
    }

    @Override
    public List<Media> getRoomMediaByHotelAndRoomType(Long hotelId, String roomType) {
        return roomRepository.getAllMediasByHotelIdAndRoomType(hotelId, roomType);
    }

    @Override
    public List<Amenity> getAllAmenitiesByHotelIdAndRoomId(Long hotelId, Long roomId) {
        return roomRepository.getAllAmenitiesByHotelIdAndRoomId(hotelId, roomId);
    }

    @Override
    public List<Amenity> getRoomAmenitiesByHotelAndRoomType(Long hotelId, String roomType) {
        return roomRepository.getAllAmenitiesByHotelIdAndRoomType(hotelId, roomType);
    }
}