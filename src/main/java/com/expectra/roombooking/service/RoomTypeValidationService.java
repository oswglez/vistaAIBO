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
// Puedes llamar a esta clase RoomTypeValidationService o incluir la lógica
// en tu RoomService o RoomTypeService existente.
public class RoomTypeValidationService {

    private static final Logger log = LoggerFactory.getLogger(RoomTypeValidationService.class);

    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    // El caché en memoria. Usamos Set para búsquedas rápidas (contains).
    private Set<String> validRoomTypeNamesCache;

    @Autowired
    public RoomTypeValidationService(RoomTypeRepository roomTypeRepository, RoomRepository roomRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
    }

    // --- Lógica del Caché ---
    @PostConstruct // Se ejecuta una vez después de que el bean es creado e inyectado
    private void initializeRoomTypeCache() {
        log.info("Initializing RoomType cache...");
        try {
            // Asume que tienes un método en tu repo que devuelve los nombres
            // o los objetos completos desde donde extraer los nombres.
            // Opción 1: Si el repo devuelve List<String> directamente
            //List<String> names = roomTypeRepository.findAllTypeNames(); // Necesitarás crear este método

            //
             List<RoomTypes> types = (List<RoomTypes>) roomTypeRepository.findAll();
             List<String> names = types.stream()
                                     .map(RoomTypes::getRoomTypeName) // Asume un getter getName()
                                     .collect(Collectors.toList());

//             Guardar en un HashSet para búsquedas eficientes O(1)
            this.validRoomTypeNamesCache = new HashSet<>(names);
            log.info("Room Type cache initialized successfully with {} types: {}",
                    validRoomTypeNamesCache.size(), validRoomTypeNamesCache);

        } catch (Exception e) {
            log.error("FATAL: Failed to initialize Room Type cache! Validation might fail.", e);
            // En caso de error, inicializar vacío para evitar NullPointerException,
            // aunque la validación no funcionará correctamente.
            this.validRoomTypeNamesCache = Collections.emptySet();
        }
    }

    // --- Método de Validación ---

    /**
     * Valida si el nombre del tipo de room proporcionado es válido
     * comparándolo contra el caché de tipos permitidos.
     * Lanza IllegalArgumentException si el tipo es inválido, nulo o vacío.
     *
     * @param typeNameToValidate El nombre del tipo a validar (ej: "IMAGE").
     * @throws IllegalArgumentException si el tipo no es válido.
     * @throws IllegalStateException si el caché no pudo ser inicializado.
     */
    public void validateType(String typeNameToValidate) {
        if (this.validRoomTypeNamesCache == null) {
            // Esto solo debería pasar si initializeRoomTypeCache falló gravemente.
            log.error("Attempted validation but Room Type cache is not initialized!");
            throw new IllegalStateException("Room Type cache is not available.");
        }

        if (typeNameToValidate == null || typeNameToValidate.isBlank()) {
            throw new IllegalArgumentException("Room Type name cannot be null or blank.");
        }

        // Comprobar si el tipo existe en el caché (distingue mayúsculas/minúsculas por defecto)
        if (!this.validRoomTypeNamesCache.contains(typeNameToValidate)) {
            log.warn("Invalid Room Type received: '{}'. Valid types are: {}", typeNameToValidate, this.validRoomTypeNamesCache);
            throw new IllegalArgumentException("Invalid Room Type provided: '" + typeNameToValidate + "'");
            // Considera crear una excepción personalizada (ej: InvalidInputDataException)
            // que pueda ser mapeada automáticamente a un 400 Bad Request por un @ControllerAdvice.
        }

        // Si .contains(typeNameToValidate) es true, el tipo es válido y el método termina.
        log.debug("Room Type validation successful for: {}", typeNameToValidate); // Log de depuración
    }
}