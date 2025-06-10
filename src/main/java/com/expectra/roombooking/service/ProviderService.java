package com.expectra.roombooking.service;

import com.expectra.roombooking.model.Hotel;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.expectra.roombooking.model.Provider;

    @Service
    public interface ProviderService {
        List<Provider> getProvidersByType(String providerType);
        Provider createProvider(Provider provider);
        Optional<Provider> getProviderById(Long providerId);
        List<Provider> getAllProviders();
        Provider updateProvider(Long providerId, Provider providerDetails);
        void deleteProvider(Long providerId);
        List<Hotel> getAllHotelsByProviderId(Long providerId);
    }

