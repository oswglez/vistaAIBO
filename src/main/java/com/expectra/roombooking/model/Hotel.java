package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "hotel")
@Data
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long hotelId;

    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_address_id", nullable = false)
    private Address hotelAddress;

    @Column(name = "local_phone")
    private String localPhone;

    @Column(name = "celular_phone")
    private String celularPhone;

    @Column(name = "pms_vendor")
    private String pmsVendor;

    @Column(name = "pms_hotel_id")
    private Long pmsHotelId;

    @Column(name = "pms_token")
    private String pmsToken; // Cambiado de Long a String

    @Column(name = "crs_vendor")
    private String crsVendor;

    @Column(name = "crs_hotel_id")
    private Long crsHotelId;

    @Column(name = "crs_token")
    private String crsToken; // Cambiado de Long a String

    @Column(name = "disclaimer")
    private String disclaimer;

    // Relación uno a muchos con Room
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms; // Representa las habitaciones asociadas

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_contact",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private Set<Contact> contacts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_amenity",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_media",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Set<Media> media = new HashSet<>();

    // Relación Many-to-Many con Address
    @ManyToMany(mappedBy = "hotels")
    private Set<Address> addresses = new HashSet<>();
}