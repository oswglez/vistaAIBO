package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "photo")
@Data
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Lob
    private byte[] photo;

    @ManyToMany(mappedBy = "photos")
    private List<Room> rooms;

    // Getters y Setters
}

