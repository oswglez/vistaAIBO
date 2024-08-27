package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity

public class RoomAmenity {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;



    @ManyToOne

    @JoinColumn(name = "room_id")

    private Room room;



    @ManyToOne

    @JoinColumn(name = "amenity_id")

    private Amenity amenity;



    // Getters and setters

}