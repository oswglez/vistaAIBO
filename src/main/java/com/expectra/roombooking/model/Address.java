package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private String country;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "address_type", nullable = false)
    @NotNull
    private String addressType;

    // Many-to-Many relationship with Contact through contact_address table
    @ManyToMany
    @JoinTable(
            name = "contact_address",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    @JsonIgnore
    private Set<Contact> contacts = new HashSet<>();

    // Utility methods to handle Contact relationship
    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.getAddresses().add(this);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.getAddresses().remove(this);
    }

    // Many-to-One relationship with Hotel
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;

    // Utility method to handle Hotel relationship
    public void setHotel(Hotel hotel) {
        if (this.hotel != null) {
            this.hotel.getAddresses().remove(this); // Clear previous relationship
        }
        this.hotel = hotel;
        if (hotel != null) {
            hotel.getAddresses().add(this); // Set new relationship
        }
    }
}