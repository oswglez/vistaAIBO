package com.expectra.roombooking.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "room")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer type;
    private String number;
    private String code;
    private Integer size;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToMany
    @JoinTable(
            name = "room_amenity",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private List<Amenity> amenities;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Video> videos;

    @ManyToMany
    @JoinTable(
            name = "room_photo",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private List<Photo> photos;

    // Getters y Setters
}

