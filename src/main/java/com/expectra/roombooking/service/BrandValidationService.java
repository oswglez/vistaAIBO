package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Brand;
import com.expectra.roombooking.repository.BrandRepository;
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
// Puedes llamar a esta clase BrandValidationService o incluir la lógica
// en tu  Brandervice existente.
public class BrandValidationService {

    private static final Logger log = LoggerFactory.getLogger(BrandValidationService.class);

    private final BrandRepository brandRepository;

    // El caché en memoria. Usamos Set para búsquedas rápidas (contains).
    private Set<String> validBrandNamesCache;

    @Autowired
    public BrandValidationService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    // --- Lógica del Caché ---
    @PostConstruct // Se ejecuta una vez después de que el bean es creado e inyectado
    private void initializeBrandCache() {
        log.info("Initializing Brand cache...");
        try {
            // Asume que tienes un método en tu repo que devuelve los nombres
            // o los objetos completos desde donde extraer los nombres.
            // Opción 1: Si el repo devuelve List<String> directamente
            //List<String> names = mediaTypeRepository.findAllTypeNames(); // Necesitarás crear este método

            // Opción 2: Si el repo devuelve List<MediaTypeEntity>
             List<Brand> brands = (List<Brand>) brandRepository.findAll();
             List<String> names = brands.stream()
                                     .map(Brand::getBrandName) // Asume un getter getName()
                                     .collect(Collectors.toList());

//             Guardar en un HashSet para búsquedas eficientes O(1)
            this.validBrandNamesCache = new HashSet<>(names);
            log.info("Media Type cache initialized successfully with {} types: {}",
                    validBrandNamesCache.size(), validBrandNamesCache);

        } catch (Exception e) {
            log.error("FATAL: Failed to initialize Brand cache! Validation might fail.", e);
            // En caso de error, inicializar vacío para evitar NullPointerException,
            // aunque la validación no funcionará correctamente.
            this.validBrandNamesCache = Collections.emptySet();
        }
    }

    // --- Método de Validación ---

    /**
     * Valida si el nombre del tipo de media proporcionado es válido
     * comparándolo contra el caché de tipos permitidos.
     * Lanza IllegalArgumentException si el tipo es inválido, nulo o vacío.
     *
     * @param nameToValidate El nombre del tipo a validar (ej: "IMAGE").
     * @throws IllegalArgumentException si el tipo no es válido.
     * @throws IllegalStateException si el caché no pudo ser inicializado.
     */
    public void validateName(String nameToValidate) {
        if (this.validBrandNamesCache == null) {
            // Esto solo debería pasar si initializeMediaTypeCache falló gravemente.
            log.error("Attempted validation but Brand cache is not initialized!");
            throw new IllegalStateException("Brand cache is not available.");
        }

        if (nameToValidate == null || nameToValidate.isBlank()) {
            throw new IllegalArgumentException("Brand name cannot be null or blank.");
        }

        // Comprobar si el tipo existe en el caché (distingue mayúsculas/minúsculas por defecto)
        if (!this.validBrandNamesCache.contains(nameToValidate)) {
            log.warn("Invalid Brand received: '{}'. Valid types are: {}", nameToValidate, this.validBrandNamesCache);
            throw new IllegalArgumentException("Invalid Brand provided: '" + nameToValidate + "'");
            // Considera crear una excepción personalizada (ej: InvalidInputDataException)
            // que pueda ser mapeada automáticamente a un 400 Bad Request por un @ControllerAdvice.
        }

        // Si .contains(typeNameToValidate) es true, el tipo es válido y el método termina.
        log.debug("Brand validation successful for: {}", nameToValidate); // Log de depuración
    }
}