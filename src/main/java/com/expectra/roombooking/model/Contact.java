package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contact")
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "contact_title", nullable = false)
    private String contactTitle;

    @Column(name = "contact_local_number", nullable = false)
    private String contactLocalNumber;

    @Column(name = "contact_mobile_number", nullable = false)
    private String contactMobileNumber;

    @Column(name = "contact_fax_number")
    private String contactFaxNumber;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    // Relación Many-to-Many con Hotel

    @ManyToMany(mappedBy = "contacts")
    @JsonIgnore
    private Set<Hotel> hotels = new HashSet<>();

    // Métodos de utilidad para manejar la relación
    public void addHotel(Hotel hotel) {
        this.hotels.add(hotel);
        hotel.getContacts().add(this);
    }

    public void removeHotel(Hotel hotel) {
        this.hotels.remove(hotel);
        hotel.getContacts().remove(this);
    }
    // Relación Many-to-Many con Address
    @ManyToMany(mappedBy = "contacts")
    @JsonIgnore
    private Set<Address> addresses = new HashSet<>();

    public void addAddress(Address address) {
        this.addresses.add(address);
        address.getContacts().add(this);
    }
    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.getContacts().remove(this);
    }
}