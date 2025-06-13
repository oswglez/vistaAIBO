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
// You can call this class ChainValidationService or include the logic
// in your ChainService existing.
public class ChainValidationService {

    private static final Logger log = LoggerFactory.getLogger(ChainValidationService.class);

    private final ChainRepository chainRepository;

    // In-memory cache. We use Set for fast lookups (contains).
    private Set<String> validChainNamesCache;

    @Autowired
    public ChainValidationService(ChainRepository chainRepository) {
        this.chainRepository = chainRepository;
    }

    // --- Cache Logic ---
    @PostConstruct // Executes once after the bean is created and injected
    private void initializeChainCache() {
        log.info("Initializing Chain cache...");
        try {
            // Assume you have a method in your repo that returns the names
            // Option 1: If the repo returns List<String> directly
            //List<String> names = chainRepository.findAllTypeNames(); // You'll need to create this method

            // Option 2: If the repo returns List<ChainEntity>
             List<Chain> chains = (List<Chain>) chainRepository.findAll();
             List<String> names = chains.stream()
                                     .map(Chain::getChainName) // Asume a getter getName()
                                     .collect(Collectors.toList());

//             Store in a HashSet for efficient O(1) lookups
            this.validChainNamesCache = new HashSet<>(names);
            log.info("Chain cache initialized successfully with {} chain names: {}",
                    validChainNamesCache.size(), validChainNamesCache);

        } catch (Exception e) {
            log.error("FATAL: Failed to initialize Chain cache! Validation might fail.", e);
            // In case of error, initialize empty to avoid NullPointerException,
            // although validation won't work correctly.
            this.validChainNamesCache = Collections.emptySet();
        }
    }

    // --- Validation Method ---

    /**
     * Validates if the provided chain type name is valid
     * by comparing it against the cache of allowed types.
     * Throws IllegalArgumentException if the type is invalid, null, or empty.
     *
     * @param nameToValidate The name of the type to validate (e.g., "IMAGE").
     * @throws IllegalArgumentException if the type is not valid.
     * @throws IllegalStateException if the cache could not be initialized.
     */
    public void validateName(String nameToValidate) {
        if (this.validChainNamesCache == null) {
            // This should only happen if initializeCache failed severely.
            log.error("Attempted validation but Chain cache is not initialized!");
            throw new IllegalStateException("Chain cache is not available.");
        }

        if (nameToValidate == null || nameToValidate.isBlank()) {
            throw new IllegalArgumentException("Chain name cannot be null or blank.");
        }

        // Check if the type exists in the cache (case-sensitive by default)
        if (!this.validChainNamesCache.contains(nameToValidate)) {
            log.warn("Invalid Chain received: '{}'. Valid types are: {}", nameToValidate, this.validChainNamesCache);
            throw new IllegalArgumentException("Invalid Chain provided: '" + nameToValidate + "'");
            // Consider creating a custom exception (e.g., InvalidInputDataException)
            // that can be automatically mapped to a 400 Bad Request by a @ControllerAdvice.
        }

        // If .contains(typeNameToValidate) is true, the type is valid and the method ends.
        log.debug("Chain validation successful for: {}", nameToValidate); // Debug log
    }
}