package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Set;

@Data
@Entity
@Table(name = "hotel")
public class Hotel {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;

    private String name;
    private Integer roomCount;
    private String fullAddress;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms;

    @ManyToMany
    @JoinTable(
            name = "hotel_amenity",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities;

    @ManyToMany
    @JoinTable(
            name = "hotel_media",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Set<Media> medias;



    // Getters and setters
}


