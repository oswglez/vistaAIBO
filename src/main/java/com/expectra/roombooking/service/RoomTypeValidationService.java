package com.expectra.roombooking.service;

import com.expectra.roombooking.model.RoomTypes;
import com.expectra.roombooking.repository.RoomRepository;
import com.expectra.roombooking.repository.RoomTypeRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
// You can call this class RoomTypeValidationService or include the logic
// in your RoomTypeService existing.
public class RoomTypeValidationService {

    private static final Logger log = LoggerFactory.getLogger(RoomTypeValidationService.class);

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    // In-memory cache. We use Set for fast lookups (contains).
    private Set<String> validRoomTypeNamesCache;

    @Autowired
    public RoomTypeValidationService(RoomTypeRepository roomTypeRepository, RoomRepository roomRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
    }

    // --- Cache Logic ---
    @PostConstruct // Executes once after the bean is created and injected
    private void initializeRoomTypeCache() {
        log.info("Initializing RoomType cache...");
        try {
            // Assume you have a method in your repo that returns the names
            // Option 1: If the repo returns List<String> directly
            //List<String> names = roomTypeRepository.findAllTypeNames(); // You'll need to create this method
            // Option 2: If the repo returns List<RoomTypeEntity>
             List<RoomTypes> types = (List<RoomTypes>) roomTypeRepository.findAll();
             List<String> names = types.stream()
                                     .map(RoomTypes::getRoomTypeName) // Asume un getter getName()
                                     .collect(Collectors.toList());

//             Store in a HashSet for efficient O(1) lookups
            this.validRoomTypeNamesCache = new HashSet<>(names);
            log.info("Room Type cache initialized successfully with {} types: {}",
                    validRoomTypeNamesCache.size(), validRoomTypeNamesCache);

        } catch (Exception e) {
            log.error("FATAL: Failed to initialize Room Type cache! Validation might fail.", e);
            // In case of error, initialize empty to avoid NullPointerException,
            // although validation won't work correctly.
            this.validRoomTypeNamesCache = Collections.emptySet();
        }
    }

    // --- Validation Method ---
    /**
     * Validates if the provided room type name is valid
     * by comparing it against the cache of allowed types.
     * Throws IllegalArgumentException if the type is invalid, null, or empty.
     *
     * @throws IllegalArgumentException if the type is not valid.
     * @throws IllegalStateException if the cache could not be initialized.
     */
    public void validateType(String typeNameToValidate) {
        if (this.validRoomTypeNamesCache == null) {
            // This should only happen if initializeCache failed severely.
            log.error("Attempted validation but Room Type cache is not initialized!");
            throw new IllegalStateException("Room Type cache is not available.");
        }

        if (typeNameToValidate == null || typeNameToValidate.isBlank()) {
            throw new IllegalArgumentException("Room Type name cannot be null or blank.");
        }

        // Check if the type exists in the cache (case-sensitive by default)
        if (!this.validRoomTypeNamesCache.contains(typeNameToValidate)) {
            log.warn("Invalid Room Type received: '{}'. Valid types are: {}", typeNameToValidate, this.validRoomTypeNamesCache);
            throw new IllegalArgumentException("Invalid Room Type provided: '" + typeNameToValidate + "'");
            // Consider creating a custom exception (e.g., InvalidInputDataException)
            // that can be automatically mapped to a 400 Bad Request by a @ControllerAdvice.
        }

        // If .contains(typeNameToValidate) is true, the type is valid and the method ends.
        log.debug("Room type validation successful for: {}", typeNameToValidate); // Debug log
    }
}