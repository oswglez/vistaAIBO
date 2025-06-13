package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="media")
@Data
@ToString(exclude = {"hotels", "rooms"})
@EqualsAndHashCode(exclude = {"hotels", "rooms"})
public class Media {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="media_id")
    private Long mediaId;

    @Column(name="media_code", nullable=false)
    private Integer mediaCode;

    @Column(name = "media_type", nullable=false)
    private String mediaType;

    @Column(name="media_description")
    private String mediaDescription;

    @Column(name="media_url")
    private String mediaUrl;

    // Many-to-Many relationship with Hotel through hotel_media table
    @ManyToMany(mappedBy="medias")
    @JsonIgnore
    private Set<Hotel> hotels = new HashSet<>();

    @ManyToMany(mappedBy="medias")
    @JsonIgnore
    private Set<Room> rooms = new HashSet<>();
}