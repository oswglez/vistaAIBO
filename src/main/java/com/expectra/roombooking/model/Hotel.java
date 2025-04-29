package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "hotel")
@Data
@ToString(exclude = {"medias", "amenities", "rooms", "contacts", "addresses"})
@EqualsAndHashCode(exclude = {"medias", "amenities", "rooms", "contacts", "addresses"})
public class    Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long hotelId;

    @NotBlank(message = "El hotel chain no puede estar vacío")
    @Column(name = "hotel_chain", nullable = false, columnDefinition = "VARCHAR DEFAULT 'Hilton'")
    private String hotelChain;

    @NotBlank(message = "El hotel brand no puede estar vacío")
    @Column(name = "hotel_brand", nullable = false, columnDefinition = "VARCHAR DEFAULT 'Sandals Resorts'")
    private String hotelBrand;

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

    @Column(name = "total_floors", nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer totalFloors;

    @Column(name = "total_rooms", nullable = false,  columnDefinition = "INTEGER DEFAULT 10")
    private Integer totalRooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FloorPlan> floorPlans = new ArrayList<>();

    // Relación uno a muchos con Room
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms = new HashSet<>(); // Representa las habitaciones asociadas

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hotel_contact",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    @JsonIgnore
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
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "hotel_amenity",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    @JsonIgnore
    private Set<Amenity> amenities = new HashSet<>();

    // Métodos de utilidad para manejar la relación con Amenity
    public void addAmenity(Amenity amenity) {
        this.amenities.add(amenity);
        amenity.getHotels().add(this);
    }

    public void removeAmenity(Amenity amenity) {
        this.amenities.remove(amenity);
        amenity.getHotels().remove(this);
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "hotel_media",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    @JsonIgnore
    private Set<Media> medias = new HashSet<>();

    // Métodos de utilidad para manejar la relación con Media
    public void addMedia(Media media) {
        this.medias.add(media);
        media.getHotels().add(this);
    }

    public void removeMedia(Media media) {
        this.medias.remove(media);
        media.getHotels().remove(this);
    }

    // Relación Many-to-Many con Address
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "hotel_address",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    @JsonIgnore
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

    // Método helper para añadir un plano de piso
    public void addFloorPlan(FloorPlan floorPlan) {
        floorPlans.add(floorPlan);
        floorPlan.setHotel(this);
    }

    // Método helper para eliminar un plano de piso
    public void removeFloorPlan(FloorPlan floorPlan) {
        floorPlans.remove(floorPlan);
        floorPlan.setHotel(null);
    }
}