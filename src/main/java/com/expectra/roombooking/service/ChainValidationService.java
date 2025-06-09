package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Chain;
import com.expectra.roombooking.repository.ChainRepository;
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
// Puedes llamar a esta clase ChainValidationService o incluir la lógica
// en tu ChainService existente.
public class ChainValidationService {

    private static final Logger log = LoggerFactory.getLogger(ChainValidationService.class);

    private final ChainRepository chainRepository;

    // El caché en memoria. Usamos Set para búsquedas rápidas (contains).
    private Set<String> validChainNamesCache;

    @Autowired
    public ChainValidationService(ChainRepository chainRepository) {
        this.chainRepository = chainRepository;
    }

    // --- Lógica del Caché ---
    @PostConstruct // Se ejecuta una vez después de que el bean es creado e inyectado
    private void initializeChainCache() {
        log.info("Initializing Chain cache...");
        try {
            // Asume que tienes un método en tu repo que devuelve los nombres
            // o los objetos completos desde donde extraer los nombres.
            // Opción 1: Si el repo devuelve List<String> directamente
            //List<String> names = chainRepository.findAllTypeNames(); // Necesitarás crear este método

            // Opción 2: Si el repo devuelve List<ChainEntity>
             List<Chain> chains = (List<Chain>) chainRepository.findAll();
             List<String> names = chains.stream()
                                     .map(Chain::getChainName) // Asume un getter getName()
                                     .collect(Collectors.toList());

//             Guardar en un HashSet para búsquedas eficientes O(1)
            this.validChainNamesCache = new HashSet<>(names);
            log.info("Chain cache initialized successfully with {} chain names: {}",
                    validChainNamesCache.size(), validChainNamesCache);

        } catch (Exception e) {
            log.error("FATAL: Failed to initialize Chain cache! Validation might fail.", e);
            // En caso de error, inicializar vacío para evitar NullPointerException,
            // aunque la validación no funcionará correctamente.
            this.validChainNamesCache = Collections.emptySet();
        }
    }

    // --- Método de Validación ---

    /**
     * Valida si el nombre del tipo de chain proporcionado es válido
     * comparándolo contra el caché de tipos permitidos.
     * Lanza IllegalArgumentException si el tipo es inválido, nulo o vacío.
     *
     * @param nameToValidate El nombre del tipo a validar (ej: "IMAGE").
     * @throws IllegalArgumentException si el tipo no es válido.
     * @throws IllegalStateException si el caché no pudo ser inicializado.
     */
    public void validateName(String nameToValidate) {
        if (this.validChainNamesCache == null) {
            // Esto solo debería pasar si initialize Cache falló gravemente.
            log.error("Attempted validation but Chain cache is not initialized!");
            throw new IllegalStateException("Chain cache is not available.");
        }

        if (nameToValidate == null || nameToValidate.isBlank()) {
            throw new IllegalArgumentException("Chain name cannot be null or blank.");
        }

        // Comprobar si el tipo existe en el caché (distingue mayúsculas/minúsculas por defecto)
        if (!this.validChainNamesCache.contains(nameToValidate)) {
            log.warn("Invalid Chain received: '{}'. Valid types are: {}", nameToValidate, this.validChainNamesCache);
            throw new IllegalArgumentException("Invalid Chain provided: '" + nameToValidate + "'");
            // Considera crear una excepción personalizada (ej: InvalidInputDataException)
            // que pueda ser mapeada automáticamente a un 400 Bad Request por un @ControllerAdvice.
        }

        // Si .contains(typeNameToValidate) es true, el tipo es válido y el método termina.
        log.debug("Chain validation successful for: {}", nameToValidate); // Log de depuración
    }
}