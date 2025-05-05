package com.expectra.roombooking.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name="chain")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Chain {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="chain_id")
    private Long chainId;

    @Column(name="chain_name", nullable=false, unique = true)
    private String chainName;

    @Column(name="chain_description", nullable=true)
    private String chainDescription;

    // Relación uno-a-muchos con Brand
    @OneToMany(mappedBy = "chain", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Brand> brands;
}
