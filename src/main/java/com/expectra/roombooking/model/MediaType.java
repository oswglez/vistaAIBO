package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="media_type")
@Data
public class MediaType {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="media_type_id")
    private Long mediaTypeId;

    @Column(name="media_name", nullable=false)
    private String mediaTypeName;
}