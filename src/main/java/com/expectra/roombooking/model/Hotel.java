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
@ToString(exclude = {"medias", "amenities", "rooms", "contacts", "addresses", "brand"})
@EqualsAndHashCode(exclude = {"medias", "amenities", "rooms", "contacts", "addresses", "brand"})
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long hotelId;

    @NotBlank(message = "Hotel code cannot be empty")
    @Column(name = "hotel_code", nullable = true)
    private String hotelCode;

    @NotBlank(message = "Hotel name cannot be empty")
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
    private String pmsToken;

    @Column(name = "crs_vendor")
    private String crsVendor;

    @Column(name = "crs_hotel_id")
    private Long crsHotelId;

    @Column(name = "crs_token")
    private String crsToken;

    @Column(name = "disclaimer")
    private String disclaimer;

    @Column(name = "total_floors", nullable = true)
    private Integer totalFloors;

    @Column(name = "total_rooms", nullable = true)
    private Integer totalRooms;

    @Column(name = "hotel_website_url", nullable = true)
    private String hotelWebsiteUrl;

    @Column(name = "hotel_deleted", nullable = true)
    private Boolean hotelDeleted;

    @Column(name = "hotel_status", nullable = true)
    private String hotelStatus;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FloorPlan> floorPlans = new ArrayList<>();

    // One-to-many relationship with Room
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms = new HashSet<>(); // Represents associated rooms

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "hotel_contact",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    @JsonIgnore
    private Set<Contact> contacts = new HashSet<>();

    // One-to-many relationship with Brand - each hotel belongs to a single brand
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_brand_id", nullable = false)
    private Brand brand;

    // Utility methods to handle Contact relationship
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "hotel_provider",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "provider_id")
    )
    @JsonIgnore
    private Set<Provider> providers = new HashSet<>();

    // Utility methods to handle Amenity relationship
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

    // Utility methods to handle Media relationship
    public void addMedia(Media media) {
        this.medias.add(media);
        media.getHotels().add(this);
    }

    public void removeMedia(Media media) {
        this.medias.remove(media);
        media.getHotels().remove(this);
    }

    // One-to-many relationship with Address
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Address> addresses = new HashSet<>();

    // Utility methods to handle Address relationship
    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setHotel(this); // Set inverse relationship
    }

    public void removeAddress(Address address) {
        this.addresses.remove(address);
        address.setHotel(null); // Clear inverse relationship
    }

    // Helper method to add a floor plan
    public void addFloorPlan(FloorPlan floorPlan) {
        floorPlans.add(floorPlan);
        floorPlan.setHotel(this);
    }

    // Helper method to remove a floor plan
    public void removeFloorPlan(FloorPlan floorPlan) {
        floorPlans.remove(floorPlan);
        floorPlan.setHotel(null);
    }
}