package com.expectra.roombooking.service;

import com.expectra.roombooking.model.AmenityType;
import com.expectra.roombooking.model.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomTypeService {
    // Operaciones básicas CRUD
    RoomType createRoomType(RoomType room);
    Optional<RoomType> getRoomTypeById(Long id);
    List<RoomType> getAllRoomType();
    RoomType updateRoomType(Long id, RoomType roomDetails);
    void deleteRoomType(Long id);
}