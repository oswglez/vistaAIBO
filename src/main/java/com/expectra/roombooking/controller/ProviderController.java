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
@Tag(name = "Provider Management", description = "Endpoints para gestión de providers para hoteles y habitaciones")

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
    @Operation(summary = "Crea un Provider", description = "Crea un provider")
    public ResponseEntity<Provider> createProvider(@RequestBody Map<String, Object> requestBody) {
        Provider provider = new Provider();
        provider.setProviderName((String) requestBody.get("providerName"));
        provider.setProviderType((String) requestBody.get("providerType"));
        provider.setProviderDescription((String) requestBody.get("providerDescription"));
        Provider createdProvider = providerService.createProvider(provider);
        return ResponseEntity.ok(createdProvider);
    }

    @GetMapping("/{providerId}")
    @Operation(summary = "Obtiene un provider", description = "Recupera un provider usando su ID.")
    public ResponseEntity<Provider> getProviderById(@PathVariable Long providerId) {
        return providerService.getProviderById(providerId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException(messageNotFound + providerId));
    }

    @GetMapping("/type")
    @Operation(summary = "Obtiene todos los providers", description = "Recupera todos los provider de un tipo dado de la base de datos.")
    public ResponseEntity<List<ProviderDTO>> getProvidersByType(
            @RequestParam @Parameter(description = "Tipo de provider para filtrar (ej: 'Hotel', 'Restaurante')", required = true) String type) {

        List<Provider> providers = providerService.getProvidersByType(type);
        List<ProviderDTO> providerDTOs = providers.stream()
                .map(provider -> modelMapper.map(provider, ProviderDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(providerDTOs);
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los providers por  type", description = "Recupera todos los provider de la base de datos dado un type")
    public ResponseEntity<List<Provider>> getAllProviders() {
        List<Provider> providers = providerService.getAllProviders();
        return ResponseEntity.ok(providers);
    }

    @PutMapping("/{providerId}")
    @Operation(summary = "Actualiza un provider", description = "Actualiza los datos de un provider existente usando su Id.")
    public ResponseEntity<Provider> updateProvider(@PathVariable Long providerId, @RequestBody Provider providerDetails) {
        Provider updatedprovider = providerService.updateProvider(providerId, providerDetails);
        return ResponseEntity.ok(updatedprovider);
    }

    @DeleteMapping("/{providerId}")
    @Operation(summary = "Elimina un provider", description = "Elimina un provider existente usando su Id.")
    public ResponseEntity<Provider> deleteProvider(@PathVariable Long providerId) {
        providerService.deleteProvider(providerId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{providerId}/hotels")
    @Operation(summary = "Obtiene todos los hoteles de un provider", description = "Recupera todos los hoteles asociados a un provider específico.")
    public ResponseEntity<ProviderDTO> getProviderWithHotels(@PathVariable Long providerId) {
        Optional<Provider> provider = providerService.getProviderById(providerId);
        List<Hotel> hotels = providerService.getAllHotelsByProviderId(providerId);

        // Mapea el Provider a ProviderDTO
        ProviderDTO providerDTO = modelMapper.map(provider, ProviderDTO.class);

        // Mapea los Hotels a HotelOnlyDTO y los asigna al ProviderDTO
        Set<HotelOnlyDTO> hotelOnlyDTOs = hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelOnlyDTO.class))
                .collect(Collectors.toSet());
        providerDTO.setHotels(hotelOnlyDTOs);

        return ResponseEntity.ok(providerDTO);
    }
}