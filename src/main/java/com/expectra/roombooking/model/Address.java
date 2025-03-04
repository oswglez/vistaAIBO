package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "address")
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

    // Relación Many-to-Many con Contact
    @ManyToMany
    @JoinTable(
            name = "contact_address", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "address_id"), // Columna que referencia a Address
            inverseJoinColumns = @JoinColumn(name = "contact_id") // Columna que referencia a Contact
    )
    private Set<Contact> contacts = new HashSet<>();

    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.getAddresses().add(this);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.getAddresses().remove(this);
    }
    // Relación Many-to-Many con Hotel
    @ManyToMany
    @JoinTable(
            name = "hotel_address",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "hotel_id")
    )
    private Set<Hotel> hotels = new HashSet<>();

    // Métodos de utilidad para manejar la relación con Hotel
    public void addHotel(Hotel hotel) {
        this.hotels.add(hotel);
        hotel.getAddresses().add(this);
    }

    public void removeHotel(Hotel hotel) {
        this.hotels.remove(hotel);
        hotel.getAddresses().remove(this);
    }

}