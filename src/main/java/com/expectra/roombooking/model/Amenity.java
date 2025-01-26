package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="amenity")
@Data
    public class Amenity {
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        @Column(name="amenity_id")
        private Long amenityId;

        @Column(name="amenity_code", nullable=false)
        private Integer amenityCode;

        @Column(name="amenity_type", nullable=false)
        private String amenityType;

        @Column(name="amenity_description")
        private String amenityDescription;

        // Relación con Hotel a través de la tabla intermedia hotel_amenity
        @ManyToMany
        @JoinTable(
                name = "hotel_amenity",
                joinColumns = @JoinColumn(name = "amenity_id"),
                inverseJoinColumns = @JoinColumn(name = "hotel_id")
        )
        private Set<Hotel> hotels = new HashSet<>();

        // Relación con Room a través de la tabla intermedia room_amenity
        @ManyToMany
        @JoinTable(
                name = "room_amenity",
                joinColumns = @JoinColumn(name = "amenity_id"),
                inverseJoinColumns = @JoinColumn(name = "room_id")
        )
        private Set<Room> rooms = new HashSet<>();
    }