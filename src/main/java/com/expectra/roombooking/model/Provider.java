package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Provider")
@Data
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_id")
    private Long providerId;

    @Column(name = "provider_name", nullable = false)
    private String providerName;

    @Column(name = "provider_type", nullable = false)
    private String providerType;

    @Column(name = "provider_description")
    private String providerDescription;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = OffsetDateTime.now();
    }
    // Relación con Hotel a través de la tabla intermedia hotel_provider
    @ManyToMany(mappedBy = "providers")
    @JsonIgnore
    private Set<Hotel> hotels = new HashSet<>();
}