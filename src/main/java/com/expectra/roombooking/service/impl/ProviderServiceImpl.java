package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import com.expectra.roombooking.repository.ProviderRepository;
import com.expectra.roombooking.model.Provider;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Override
    public List<Provider> getProvidersByType(String providerType) {
        return providerRepository.findByProviderType(providerType);
    }

    @Override
    public Provider createProvider(Provider provider) {
        return providerRepository.save(provider);
    }

    @Override
    public Optional<Provider> getProviderById(Long providerId) {
        return providerRepository.findById(providerId);
    }

    @Override
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    @Override
    public Provider updateProvider(Long providerId, Provider providerDetails) {
        Provider provider1 = providerRepository.findById(providerId)
                .map(provider -> {
                    provider.setProviderName(providerDetails.getProviderName());
                    provider.setProviderType(providerDetails.getProviderType());
                    provider.setProviderDescription(providerDetails.getProviderDescription());
                    return providerRepository.save(provider);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Provider not found with ID: " + providerId));
        return provider1;
    }

    @Override
    public void deleteProvider(Long providerId) {
        providerRepository.deleteById(providerId);
    }

    @Override
    public List<Hotel> getAllHotelsByProviderId(Long providerId) {
        return providerRepository.findAllHotelsByProviderId(providerId);
    }
}


