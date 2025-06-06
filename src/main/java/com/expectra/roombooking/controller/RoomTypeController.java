package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.RoomTypes;
import com.expectra.roombooking.service.RoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roomType")
@Tag(name = "Room Management", description = "Endpoints para gestión de amenities para hoteles y habitaciones")

@CrossOrigin(origins = "*")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private final String messageNotFound = "Room not found with ID: ";

    @Autowired
    public RoomTypeController(final RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @PostMapping
    @Operation(summary = "Crea una roomType", description = "Crea una roomType usando su ID.")
    public ResponseEntity<RoomTypes> createRoomType(@RequestBody Map<String, Object> requestBody) {
        RoomTypes roomTypes = new RoomTypes();
        roomTypes.setRoomTypeName((String) requestBody.get("roomTypeName"));

        RoomTypes createdRoomTypes = roomTypeService.createRoomType(roomTypes);
        return ResponseEntity.ok(createdRoomTypes);
    }

    @GetMapping("/{roomTypeId}")
    @Operation(summary = "Obtiene una roomType", description = "Recupera una roomType usando su ID.")
    public ResponseEntity<RoomTypes> getRoomTypeByIdById(@PathVariable Long roomTypeId) {
        return roomTypeService.getRoomTypeById(roomTypeId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + roomTypeId));
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los roomTypes", description = "Recupera todos los roomTypes de la base de datos.")
    public ResponseEntity<List<RoomTypes>> getAllRoomTypes() {
        List<RoomTypes> roomTypes = roomTypeService.getAllRoomType();
        return ResponseEntity.ok(roomTypes);
    }

    @PutMapping("/{roomTypeId}")
    @Operation(summary = "Actualiza una roomType", description = "Actualiza los datos de una roomType existente usando su Id.")
    public ResponseEntity<RoomTypes> updateRoomType(@PathVariable Long roomTypeId, @RequestBody RoomTypes roomTypesDetails) {
        RoomTypes updatedRoomType = roomTypeService.updateRoomType(roomTypeId, roomTypesDetails);
        return ResponseEntity.ok(updatedRoomType);
    }

    @DeleteMapping("/{roomTypeId}")
    @Operation(summary = "" +
            "Elimina una roomType", description = "Elimina una roomType existente usando su Id.")
    public ResponseEntity<RoomTypes> deleteRoomType(@PathVariable Long roomTypeId) {
        roomTypeService.deleteRoomType(roomTypeId);
        return ResponseEntity.noContent().build();
    }
}