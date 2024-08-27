package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Lob
    private byte[] video;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    // Getters y Setters
}

