package com.expectra.roombooking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hotel")
@Data
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long hotelId;

    @NotBlank(message = "El nombre del hotel no puede estar vacío")
    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

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
    private Set<Room> rooms = new HashSet<>(); // Representa las habitaciones asociadas

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_contact",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    private Set<Contact> contacts = new HashSet<>();

    // Métodos de utilidad para manejar la relación con Contact
    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.getHotels().add(this);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.getHotels().remove(this);
    }
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_amenity",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_media",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Set<Media> media = new HashSet<>();

    // Relación Many-to-Many con Address
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_address",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Set<Address> addresses = new HashSet<>();


    // Métodos de utilidad para manejar la relación con Address
    public void addAddress(Address address) {
        this.addresses.add(address);
        address.getHotels().add(this);
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.getHotels().remove(this);
    }
}