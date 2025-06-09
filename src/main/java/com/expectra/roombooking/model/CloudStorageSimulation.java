package com.expectra.roombooking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "cloud_storage_simulation")
public class CloudStorageSimulation {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url", nullable = false, length = Integer.MAX_VALUE)
    private String url;

    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    @Column(name = "media_type", nullable = false, length = Integer.MAX_VALUE)
    private String mediaType;

}