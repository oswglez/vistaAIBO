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

    @Column(name="brand_name", nullable=false, unique = true)
    private String brandName;


    @Column(name="brand_description", nullable=true)
    private String brandDescription;

    // Relación muchos-a-uno con Chain
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_chain_id", nullable = false)
    private Chain chain;
}
