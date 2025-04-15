package com.expectra.roombooking.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="amenity_type")
@Data
public class AmenityType {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="amenity_type_id")
    private Long amenityTypeId;

    @Column(name="amenity_name", nullable=false)
    private String amenityTypeName;
}
