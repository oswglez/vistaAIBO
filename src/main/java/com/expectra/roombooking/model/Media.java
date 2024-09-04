package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Check;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "media")
@Check(constraints = "media_type IN ('IMAGE', 'VIDEO', 'AUDIO')")
public class Media {

    @Id // Solo usa jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;
    private String mediaType;
    private String url;

    @ManyToMany(mappedBy = "medias")
    private Set<Hotel> hotels = new LinkedHashSet<>(); // Usa java.util.Set

    @ManyToMany(mappedBy = "media")
    private Set<Room> rooms = new LinkedHashSet<>(); // Usa java.util.Set

    // No necesitas otra relación con el mismo nombre (hotels) y tipo diferente

    // Getters and setters (Lombok se encarga con @Data)
}
