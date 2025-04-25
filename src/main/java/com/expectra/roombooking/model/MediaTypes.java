package com.expectra.roombooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="media_types")
@Data
public class MediaTypes {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="media_types_id")
    private Long mediaTypeId;

    @Column(name="media_types_name", nullable=false)
    private String mediaTypeName;
}