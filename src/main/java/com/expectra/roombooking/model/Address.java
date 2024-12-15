package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Address")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "country", nullable = false)
    private Integer country;

    @Column(name = "state", nullable = false)
    private Integer state;

    @Column(name = "city", nullable = false)
    private Integer city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;
}