package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.RoomTypes;
import com.expectra.roombooking.service.RoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/roomType")
@Tag(name = "Room Management", description = "Endpoints for managing hotel and room amenities")

@CrossOrigin(origins = "*")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private final String messageNotFound = "Room not found with ID: ";

    @Autowired
    public RoomTypeController(final RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @PostMapping
    @Operation(summary = "Create roomType", description = "Create a roomType by ID.")
    public ResponseEntity<RoomTypes> createRoomType(@RequestBody Map<String, Object> requestBody) {
        RoomTypes roomTypes = new RoomTypes();
        roomTypes.setRoomTypeName((String) requestBody.get("roomTypeName"));
        roomTypes.setRoomTypeDescription((String) requestBody.get("roomTypeDescription"));

        RoomTypes createdRoomTypes = roomTypeService.createRoomType(roomTypes);
        return ResponseEntity.ok(createdRoomTypes);
    }

    @GetMapping("/{roomTypeId}")
    @Operation(summary = "Get a roomType by Id", description = "Get a roomType using its ID.")
    public ResponseEntity<RoomTypes> getRoomTypeByIdById(@PathVariable Long roomTypeId) {
        return roomTypeService.getRoomTypeById(roomTypeId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + roomTypeId));
    }

    @GetMapping
    @Operation(summary = "Get all roomTypes", description = "Get all roomTypes from the database.")
    public ResponseEntity<Page<RoomTypes>> getAllRoomTypes(
            @PageableDefault(page = 0, size = 10) // Default pagination values (0-indexed page)
            @SortDefault(sort = "roomTypeName", direction = Sort.Direction.ASC) // Default sorting
            Pageable pageable) {
        Page<RoomTypes> roomTypes = roomTypeService.getAllRoomType(pageable);
        return ResponseEntity.ok(roomTypes);
    }

    @PutMapping("/{roomTypeId}")
    @Operation(summary = "Update a roomType", description = "Update room type by Id")
    public ResponseEntity<RoomTypes> updateRoomType(@PathVariable Long roomTypeId, @RequestBody RoomTypes roomTypesDetails) {
        RoomTypes updatedRoomType = roomTypeService.updateRoomType(roomTypeId, roomTypesDetails);
        return ResponseEntity.ok(updatedRoomType);
    }

    @DeleteMapping("/{roomTypeId}")
    @Operation(summary = "" +
    "Delete roomType", description = "Delete roomType using Id.")
    public ResponseEntity<RoomTypes> deleteRoomType(@PathVariable Long roomTypeId) {
        roomTypeService.deleteRoomType(roomTypeId);
        return ResponseEntity.noContent().build();
    }
}