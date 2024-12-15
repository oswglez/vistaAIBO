package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;


@Entity
@Table(name = "Contact")
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

    @Column(name = "address_id", nullable = false)
    private String addresId;

    // Relación con Hotel a través de la tabla intermedia hotel_contact
    @ManyToMany(mappedBy = "contacts")
    private Set<Hotel> hotels;
}