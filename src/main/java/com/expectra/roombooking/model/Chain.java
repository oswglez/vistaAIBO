package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chain")
@Data
public class Chain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chain_id")
    private Long chainId;

    @Column(name = "chain_name", nullable = false, unique = true)
    private String chainName;

    @Column(name = "chain_description")
    private String chainDescription;

    // One-to-Many relationship with Brand
    @OneToMany(mappedBy = "chain", cascade = CascadeType.ALL)
    private Set<Brand> brands = new HashSet<>();
}
