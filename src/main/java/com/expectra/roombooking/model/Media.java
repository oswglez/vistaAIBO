package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="media")
@Data
public class Media {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="media_id")
    private Long mediaId;

    @Column(name="media_code", nullable=false)
    private Integer mediaCode;

    @Enumerated(EnumType.STRING) // Almacena el valor del Enum como una cadena en la base de datos
    @Column(name="media_type")
    private MediaType mediaType;

    @Column(name="media_description")
    private String mediaDescription;

    @Column(name="media_url")
    private String mediaUrl;

    // Relación con Hotel a través de la tabla intermedia hotel_media
    @ManyToMany(mappedBy="medias")
    @JsonIgnore
    private Set<Hotel> hotels = new HashSet<>();

    @ManyToMany(mappedBy="medias")
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();

}