package com.expectra.roombooking.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="brand")
@Data
public class Brand {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="brand_id")
    private Long brandId;

    @Column(name="brand_name", nullable=false)
    private String brandName;


    @Column(name="brand_description", nullable=true)
    private String brandDescription;
}
