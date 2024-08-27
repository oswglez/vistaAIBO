package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
@Data
@Entity

public class Room {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne

    @JoinColumn(name = "hotel_id")

    private Hotel hotel;

    private String roomType;

    private String size;



    @OneToMany(mappedBy = "room")

    private List<RoomMedia> roomMedia;



    @OneToMany(mappedBy = "room")

    private List<RoomAmenity> roomAmenities;


    // Getters and setters

}