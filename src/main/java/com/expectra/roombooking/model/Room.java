package com.expectra.roombooking.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="room")
@Data
public class Room {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long roomId;

    @Column(name="room_number", nullable=false)
    private Integer roomNumber;

    @Column(name="room_type", nullable=false) // Tipo de habitación, puedes definir un Enum si es necesario.
    private Integer roomType;

    @Column(name="room_name")
    private String roomName;

    // Relación con Hotel (1:N) - cada habitación pertenece a un solo hotel.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_hotel_id", nullable = false)
    private Hotel hotel;

    // Relación con Amenity a través de la tabla intermedia room_amenity.
    @ManyToMany
    @JoinTable(
            name="room_amenity",
            joinColumns=@JoinColumn(name="room_id"),
            inverseJoinColumns=@JoinColumn(name="amenity_id"))
    private Set<Amenity> amenities; // Relación con Amenity

    // Relación con Media a través de la tabla intermedia room_media.
    @ManyToMany
    @JoinTable(
            name="room_media",
            joinColumns=@JoinColumn(name="room_id"),
            inverseJoinColumns=@JoinColumn(name="media_id"))
    private Set<Media> media = new HashSet<>();

}