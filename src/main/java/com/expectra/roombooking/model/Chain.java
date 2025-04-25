package com.expectra.roombooking.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="chain")
@Data
public class Chain {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="chain_id")
    private Long chainId;

    @Column(name="chain_name", nullable=false)
    private String chainName;


    @Column(name="chain_description", nullable=true)
    private String chainDescription;
}
