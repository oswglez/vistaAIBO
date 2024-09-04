package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Set;

@Data
@Entity
@Table(name = "room")
public class Room {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private String roomType;
    private String size;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToMany
    @JoinTable(
            name = "room_amenity",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities;

    @ManyToMany
    @JoinTable(
            name = "room_media",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Set<Media> media;


    // Getters and setters
}