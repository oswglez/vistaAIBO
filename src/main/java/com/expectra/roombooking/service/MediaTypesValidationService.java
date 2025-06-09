package com.expectra.roombooking.service;

import com.expectra.roombooking.model.MediaTypes;
import com.expectra.roombooking.repository.MediaTypesRepository;
import jakarta.annotation.PostConstruct; // Import para PostConstruct
import org.slf4j.Logger;                 // Import para Logging (recomendado)
import org.slf4j.LoggerFactory;         // Import para Logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
// Puedes llamar a esta clase MediaTypeValidationService o incluir la lógica
// en tu MediaService o MediaTypeService existente.
public class MediaTypesValidationService {

    private static final Logger log = LoggerFactory.getLogger(MediaTypesValidationService.class);

    private final MediaTypesRepository mediaTypesRepository;



    // El caché en memoria. Usamos Set para búsquedas rápidas (contains).
    private Set<String> validMediaTypeNamesCache;

    @Autowired
    public MediaTypesValidationService(MediaTypesRepository mediaTypesRepository) {
        this.mediaTypesRepository = mediaTypesRepository;
    }

    // --- Lógica del Caché ---
    @PostConstruct // Se ejecuta una vez después de que el bean es creado e inyectado
    private void initializeMediaTypeCache() {
        log.info("Initializing Media Type cache...");
        try {
            // Asume que tienes un método en tu repo que devuelve los nombres
            // o los objetos completos desde donde extraer los nombres.
            // Opción 1: Si el repo devuelve List<String> directamente
            //List<String> names = mediaTypeRepository.findAllTypeNames(); // Necesitarás crear este método

            // Opción 2: Si el repo devuelve List<MediaTypeEntity>
             List<MediaTypes> types = (List<MediaTypes>) mediaTypesRepository.findAll();
             List<String> names = types.stream()
                                     .map(MediaTypes::getMediaTypeName) // Asume un getter getName()
                                     .collect(Collectors.toList());

//             Guardar en un HashSet para búsquedas eficientes O(1)
            this.validMediaTypeNamesCache = new HashSet<>(names);
            log.info("Media Type cache initialized successfully with {} types: {}",
                    validMediaTypeNamesCache.size(), validMediaTypeNamesCache);

        } catch (Exception e) {
            log.error("FATAL: Failed to initialize Media Type cache! Validation might fail.", e);
            // En caso de error, inicializar vacío para evitar NullPointerException,
            // aunque la validación no funcionará correctamente.
            this.validMediaTypeNamesCache = Collections.emptySet();
        }
    }

    // --- Método de Validación ---

    /**
     * Valida si el nombre del tipo de media proporcionado es válido
     * comparándolo contra el caché de tipos permitidos.
     * Lanza IllegalArgumentException si el tipo es inválido, nulo o vacío.
     *
     * @param typeNameToValidate El nombre del tipo a validar (ej: "IMAGE").
     * @throws IllegalArgumentException si el tipo no es válido.
     * @throws IllegalStateException si el caché no pudo ser inicializado.
     */
    public void validateType(String typeNameToValidate) {
        if (this.validMediaTypeNamesCache == null) {
            // Esto solo debería pasar si initializeMediaTypeCache falló gravemente.
            log.error("Attempted validation but Media Type cache is not initialized!");
            throw new IllegalStateException("Media Type cache is not available.");
        }

        if (typeNameToValidate == null || typeNameToValidate.isBlank()) {
            throw new IllegalArgumentException("Media Type name cannot be null or blank.");
        }

        // Comprobar si el tipo existe en el caché (distingue mayúsculas/minúsculas por defecto)
        if (!this.validMediaTypeNamesCache.contains(typeNameToValidate)) {
            log.warn("Invalid Media Type received: '{}'. Valid types are: {}", typeNameToValidate, this.validMediaTypeNamesCache);
            throw new IllegalArgumentException("Invalid Media Type provided: '" + typeNameToValidate + "'");
            // Considera crear una excepción personalizada (ej: InvalidInputDataException)
            // que pueda ser mapeada automáticamente a un 400 Bad Request por un @ControllerAdvice.
        }

        // Si .contains(typeNameToValidate) es true, el tipo es válido y el método termina.
        log.debug("Media Type validation successful for: {}", typeNameToValidate); // Log de depuración
    }
}