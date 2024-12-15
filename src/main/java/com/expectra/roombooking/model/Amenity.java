package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name="Amenity")
@Getter
@Setter
public class Amenity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="amenity_id")
    private Long amenityId;

    @Column(name="amenity_code", nullable=false)
    private Integer amenityCode;

    @Column(name="amenity_type", nullable=false)
    private String amenityType;

    @Column(name="amenity_description")
    private String amenityDescription;

    // Relación con Hotel a través de la tabla intermedia hotel_amenity
    @ManyToMany(mappedBy="amenities")
    private Set<Hotel> hotels; // Relación con Hotel

    // Relación con Room a través de la tabla intermedia room_amenity
    @ManyToMany(mappedBy="amenities")
    private Set<Room> rooms; // Relación con Room
}