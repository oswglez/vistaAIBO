package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "room")
@Data
@ToString(exclude = {"medias", "amenities"})
@EqualsAndHashCode(exclude = {"medias", "amenities"})
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_number", nullable = false)
    private Integer roomNumber;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_description")
    private String roomDescription;

    @Column(name = "room_building")
    private String roomBuildingName;

    @Column(name = "room_building_code", nullable = false)
    private String roomBuildingCode;

    @Column(name = "room_floor", nullable = false)
    private Integer roomFloor;

    @Column(name = "room_x_coordinates")
    private String roomXCoordinates;

    @Column(name = "room_y_coordinates")
    private String roomYCoordinates;

    @Column(name = "room_price", nullable = false, columnDefinition = "DOUBLE DEFAULT 100.0")
    private Double roomPrice;

    // Many-to-One relationship with Hotel - each room belongs to a single hotel
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_hotel_id", nullable = false)
    @JsonIgnore
    private Hotel hotel;

    // Many-to-Many relationship with Amenity through room_amenity table
    @ManyToMany
    @JoinTable(
            name = "room_amenity",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    @JsonIgnore
    private Set<Amenity> amenities = new HashSet<>();

    // Utility methods to handle Amenity relationship
    public void addAmenity(Amenity amenity) {
        this.amenities.add(amenity);
        amenity.getRooms().add(this);
    }

    public void removeAmenity(Amenity amenity) {
        this.amenities.remove(amenity);
        amenity.getRooms().remove(this);
    }

    // Many-to-Many relationship with Media through room_media table
    @ManyToMany
    @JoinTable(
            name = "room_media",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id"))
    @JsonIgnore
    private Set<Media> medias = new HashSet<>();

    // Utility methods to handle Media relationship
    public void addMedia(Media media) {
        this.medias.add(media);
        media.getRooms().add(this);
    }

    public void removeMedia(Media media) {
        this.medias.remove(media);
        media.getRooms().remove(this);
    }
}