package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "amenity")
@Data
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_id")
    private Long amenityId;

    @Column(name = "amenity_code", nullable = false)
    private String amenityCode;

    @Column(name = "amenity_type", nullable = false)
    private String amenityType;

    @Column(name = "amenity_description")
    private String amenityDescription;

    // Many-to-Many relationship with Hotel through hotel_amenity table
    @ManyToMany(mappedBy = "amenities")
    @JsonIgnore
    private Set<Hotel> hotels = new HashSet<>();

    // Many-to-Many relationship with Room through room_amenity table
    @ManyToMany(mappedBy = "amenities")
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();
}