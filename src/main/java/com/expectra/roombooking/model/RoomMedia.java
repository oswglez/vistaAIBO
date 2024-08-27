package com.expectra.roombooking.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

@Entity

@IdClass(RoomMedia.class)

public class RoomMedia {
    @jakarta.persistence.Id
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;



    @ManyToOne

    @JoinColumn(name = "room_id")

    private Room room;



    @ManyToOne

    @JoinColumn(name = "media_id")

    private Media media;



    // Getters and setters

}
