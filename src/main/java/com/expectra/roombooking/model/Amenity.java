package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "amenity")
@Data
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String description;

    @ManyToMany(mappedBy = "amenities")
    private List<Hotel> hotels;

    @ManyToMany(mappedBy = "amenities")
    private List<Room> rooms;

    // Getters y Setters
}
