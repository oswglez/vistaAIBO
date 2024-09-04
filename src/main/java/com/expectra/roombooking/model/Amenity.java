package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity

@Table(name = "amenity")
public class Amenity {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amenityId;
    private String code;
    private String description;

    @ManyToMany(mappedBy = "amenities")
    private Set<Hotel> hotels;

    @ManyToMany(mappedBy = "amenities")
    private Set<Room> rooms;


    // Getters and setters
}