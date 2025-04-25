package com.expectra.roombooking.service;

import com.expectra.roombooking.model.AmenityTypes;
import com.expectra.roombooking.repository.AmenityRepository;
import com.expectra.roombooking.repository.AmenityTypeRepository;
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
// Puedes llamar a esta clase AmenityTypeValidationService o incluir la lógica
// en tu AmenityService o AmenityTypeService existente.
public class AmenityTypeValidationService {

    private static final Logger log = LoggerFactory.getLogger(AmenityTypeValidationService.class);

    private final AmenityTypeRepository amenityTypeRepository;
    private final AmenityRepository amenityRepository;

    // El caché en memoria. Usamos Set para búsquedas rápidas (contains).
    private Set<String> validAmenityTypeNamesCache;

    @Autowired
    public AmenityTypeValidationService(AmenityTypeRepository amenityTypeRepository, AmenityRepository amenityRepository) {
        this.amenityTypeRepository = amenityTypeRepository;
        this.amenityRepository = amenityRepository;
    }

    // --- Lógica del Caché ---
    @PostConstruct // Se ejecuta una vez después de que el bean es creado e inyectado
    private void initializeAmenityTypeCache() {
        log.info("Initializing AmenityType cache...");
        try {
            // Asume que tienes un método en tu repo que devuelve los nombres
            // o los objetos completos desde donde extraer los nombres.
            // Opción 1: Si el repo devuelve List<String> directamente
            //List<String> names = amenityTypeRepository.findAllTypeNames(); // Necesitarás crear este método

            //
             List<AmenityTypes> types = (List<AmenityTypes>) amenityTypeRepository.findAll();
             List<String> names = types.stream()
                                     .map(AmenityTypes::getAmenityTypeName) // Asume un getter getName()
                                     .collect(Collectors.toList());

//             Guardar en un HashSet para búsquedas eficientes O(1)
            this.validAmenityTypeNamesCache = new HashSet<>(names);
            log.info("Amenity Type cache initialized successfully with {} types: {}",
                    validAmenityTypeNamesCache.size(), validAmenityTypeNamesCache);

        } catch (Exception e) {
            log.error("FATAL: Failed to initialize Amenity Type cache! Validation might fail.", e);
            // En caso de error, inicializar vacío para evitar NullPointerException,
            // aunque la validación no funcionará correctamente.
            this.validAmenityTypeNamesCache = Collections.emptySet();
        }
    }

    // --- Método de Validación ---

    /**
     * Valida si el nombre del tipo de amenity proporcionado es válido
     * comparándolo contra el caché de tipos permitidos.
     * Lanza IllegalArgumentException si el tipo es inválido, nulo o vacío.
     *
     * @param typeNameToValidate El nombre del tipo a validar (ej: "IMAGE").
     * @throws IllegalArgumentException si el tipo no es válido.
     * @throws IllegalStateException si el caché no pudo ser inicializado.
     */
    public void validateType(String typeNameToValidate) {
        if (this.validAmenityTypeNamesCache == null) {
            // Esto solo debería pasar si initializeAmenityTypeCache falló gravemente.
            log.error("Attempted validation but Amenity Type cache is not initialized!");
            throw new IllegalStateException("Amenity Type cache is not available.");
        }

        if (typeNameToValidate == null || typeNameToValidate.isBlank()) {
            throw new IllegalArgumentException("Amenity Type name cannot be null or blank.");
        }

        // Comprobar si el tipo existe en el caché (distingue mayúsculas/minúsculas por defecto)
        if (!this.validAmenityTypeNamesCache.contains(typeNameToValidate)) {
            log.warn("Invalid Amenity Type received: '{}'. Valid types are: {}", typeNameToValidate, this.validAmenityTypeNamesCache);
            throw new IllegalArgumentException("Invalid Amenity Type provided: '" + typeNameToValidate + "'");
            // Considera crear una excepción personalizada (ej: InvalidInputDataException)
            // que pueda ser mapeada automáticamente a un 400 Bad Request por un @ControllerAdvice.
        }

        // Si .contains(typeNameToValidate) es true, el tipo es válido y el método termina.
        log.debug("Amenity Type validation successful for: {}", typeNameToValidate); // Log de depuración
    }
}