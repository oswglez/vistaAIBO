package com.expectra.roombooking.controller;

import com.expectra.roombooking.dto.HotelOnlyDTO;
import com.expectra.roombooking.dto.ProviderDTO;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Provider;
import com.expectra.roombooking.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/provider")
@Tag(name = "Provider Management", description = "Endpoints for managing hotel and room providers")

@CrossOrigin(origins = "*")
public class ProviderController {

    private final ProviderService providerService;
    private final ModelMapper modelMapper;

    private final String messageNotFound = "Provider not found with ID: ";

    @Autowired
    public ProviderController(final ProviderService providerService, ModelMapper modelMapper) {
        this.providerService = providerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @Operation(summary = "Create a provider", description = "Creates a provider")
    public ResponseEntity<Provider> createProvider(@RequestBody Map<String, Object> requestBody) {
        Provider provider = new Provider();
        provider.setProviderName((String) requestBody.get("providerName"));
        provider.setProviderType((String) requestBody.get("providerType"));
        provider.setProviderDescription((String) requestBody.get("providerDescription"));
        Provider createdProvider = providerService.createProvider(provider);
        return ResponseEntity.ok(createdProvider);
    }

    @GetMapping("/{providerId}")
    @Operation(summary = "Get a provider", description = "Retrieves a provider using its ID.")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long providerId) {
        return providerService.getProviderById(providerId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + providerId));
    }

    @GetMapping("/type")
    @Operation(summary = "Get all providers", description = "Retrieves all providers of a given type from the database.")
    public ResponseEntity<List<ProviderDTO>> getProvidersByType(
            @RequestParam @Parameter(description = "Provider type to filter (e.g., 'Hotel', 'Restaurant')", required = true) String type) {

        List<Provider> providers = providerService.getProvidersByType(type);
        List<ProviderDTO> providerDTOs = providers.stream()
                .map(provider -> modelMapper.map(provider, ProviderDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(providerDTOs);
    }

    @GetMapping
    @Operation(summary = "Get all providers by type", description = "Retrieves all providers from the database for a given type")
    public ResponseEntity<List<Provider>> getAllProviders() {
        List<Provider> providers = providerService.getAllProviders();
        return ResponseEntity.ok(providers);
    }

    @PutMapping("/{providerId}")
    @Operation(summary = "Update a provider", description = "Updates the data of an existing provider using its ID.")
    public ResponseEntity<Provider> updateProvider(@PathVariable Long providerId, @RequestBody Provider providerDetails) {
        Provider updatedprovider = providerService.updateProvider(providerId, providerDetails);
        return ResponseEntity.ok(updatedprovider);
    }

    @DeleteMapping("/{providerId}")
    @Operation(summary = "Delete a provider", description = "Deletes an existing provider using its ID.")
    public ResponseEntity<Provider> deleteProvider(@PathVariable Long providerId) {
        providerService.deleteProvider(providerId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{providerId}/hotels")
    @Operation(summary = "Get hotels by provider", description = "Retrieves all hotels associated with a specific provider.")
    public ResponseEntity<ProviderDTO> getProviderWithHotels(@PathVariable Long providerId) {
        Optional<Provider> provider = providerService.getProviderById(providerId);
        List<Hotel> hotels = providerService.getAllHotelsByProviderId(providerId);

        // Map Provider to ProviderDTO
        ProviderDTO providerDTO = modelMapper.map(provider, ProviderDTO.class);

        // Map Hotels to HotelOnlyDTO and assign them to ProviderDTO
        Set<HotelOnlyDTO> hotelOnlyDTOs = hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelOnlyDTO.class))
                .collect(Collectors.toSet());
        providerDTO.setHotels(hotelOnlyDTOs);

        return ResponseEntity.ok(providerDTO);
    }
}