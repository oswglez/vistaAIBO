package com.expectra.roombooking.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
    public class ProviderDTO implements Serializable {
        private Long providerId;
        private String providerName;
        private String providerType;
        private String providerDescription;
        private OffsetDateTime createdAt = OffsetDateTime.now();
        private OffsetDateTime updatedAt = OffsetDateTime.now();

        private Set<HotelOnlyDTO> hotels;
    }
