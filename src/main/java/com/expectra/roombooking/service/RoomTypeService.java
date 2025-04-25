package com.expectra.roombooking.service;


import com.expectra.roombooking.model.RoomTypes;

import java.util.List;
import java.util.Optional;

public interface RoomTypeService {
    // Operaciones básicas CRUD
    RoomTypes createRoomType(RoomTypes room);
    Optional<RoomTypes> getRoomTypeById(Long id);
    List<RoomTypes> getAllRoomType();
    RoomTypes updateRoomType(Long id, RoomTypes roomDetails);
    void deleteRoomType(Long id);
}