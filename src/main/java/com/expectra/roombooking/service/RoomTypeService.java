package com.expectra.roombooking.service;

import com.expectra.roombooking.model.RoomTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RoomTypeService {
    // Operaciones básicas CRUD
    RoomTypes createRoomType(RoomTypes room);
    Optional<RoomTypes> getRoomTypeById(Long id);
    Page<RoomTypes> getAllRoomType(Pageable pageable);
    RoomTypes updateRoomType(Long id, RoomTypes roomDetails);
    void deleteRoomType(Long id);
}