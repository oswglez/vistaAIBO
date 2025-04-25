package com.expectra.roombooking.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="amenity_types")
@Data
public class AmenityTypes {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="amenity_types_id")
    private Long amenityTypeId;

    @Column(name="amenity_types_name", nullable=false)
    private String amenityTypeName;
}
