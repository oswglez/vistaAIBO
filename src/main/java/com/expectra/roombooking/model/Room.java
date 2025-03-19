package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="room")
@Data
@ToString(exclude = {"medias", "amenities"}) // Excluye colecciones
@EqualsAndHashCode(exclude = {"medias", "amenities"}) // Excluye colecciones
public class Room {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long roomId;

    @Column(name="room_number", nullable=false)
    private Integer roomNumber;

    @Enumerated(EnumType.STRING) // Almacena el valor del Enum como una cadena en la base de datos
    @Column(name="room_type", nullable=false) // Tipo de habitación, puedes definir un Enum si es necesario.
    private RoomType roomType;

    @Column(name="room_name")
    private String roomName;

    @Column(name = "room_building", columnDefinition = "VARCHAR DEFAULT 'Main'")
    private String roomBuilding;

    @Column(name = "room_floor", nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer roomFloor;

    // Relación con Hotel (1:N) - cada habitación pertenece a un solo hotel.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_hotel_id", nullable = false)
    @JsonIgnore
    private Hotel hotel;

    // Relación con Amenity a través de la tabla intermedia room_amenity.
    @ManyToMany
    @JoinTable(
            name="room_amenity",
            joinColumns=@JoinColumn(name="room_id"),
            inverseJoinColumns=@JoinColumn(name="amenity_id"))
    @JsonIgnore
    private Set<Amenity> amenities; // Relación con Amenity

    // Métodos de utilidad para manejar la relación con Amenity
    public void addAmenity(Amenity amenity) {
        this.amenities.add(amenity);
        amenity.getRooms().add(this);
    }

    public void removeAmenity(Amenity amenity) {
        this.amenities.remove(amenity);
        amenity.getRooms().remove(this);
    }

    // Relación con Media a través de la tabla intermedia room_media.
    @ManyToMany
    @JoinTable(
            name="room_media",
            joinColumns=@JoinColumn(name="room_id"),
            inverseJoinColumns=@JoinColumn(name="media_id"))
    @JsonIgnore
    private Set<Media> medias = new HashSet<>();

    // Métodos de utilidad para manejar la relación con Media
    public void addMedia(Media media) {
        this.medias.add(media);
        media.getRooms().add(this);
    }

    public void removeMedia(Media media) {
        this.medias.remove(media);
        media.getRooms().remove(this);
    }
}