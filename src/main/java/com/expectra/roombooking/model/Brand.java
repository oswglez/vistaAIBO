package com.expectra.roombooking.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="brand")
@Data
@ToString(exclude = {"hotels", "chain"})
@EqualsAndHashCode(exclude = {"hotels", "chain"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

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

    // Relacion uno-a-muchos con Hotel
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Hotel> hotels = new HashSet<>();
}
