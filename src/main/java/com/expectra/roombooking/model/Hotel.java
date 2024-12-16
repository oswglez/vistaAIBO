package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "Hotel")
@Data
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long hotelId;

    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_address_id", nullable = false) // Clave foránea hacia Address
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
    private String pmsToken;

    @Column(name = "crs_vendor")
    private String crsVendor;

    @Column(name = "crs_hotel_id")
    private Long crsHotelId;

    @Column(name = "crs_token")
    private String crsToken;

    @Column(name = "disclaimer")
    private String disclaimer;
    @ManyToMany
    @JoinTable(
            name = "hotel_contact",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private Set<Contact> contacts;

    @ManyToMany
    @JoinTable(
            name="hotel_amenity",
            joinColumns=@JoinColumn(name="hotel_id"),
            inverseJoinColumns=@JoinColumn(name="amenity_id"))
    private Set<Amenity> amenities; // Relación con Amenity

    // Relación con Media a través de la tabla intermedia hotel_media
    @ManyToMany
    @JoinTable(
            name="hotel_media",
            joinColumns=@JoinColumn(name="hotel_id"),
            inverseJoinColumns=@JoinColumn(name="media_id"))
    private Set<Media> media; // Relación con Media
}

