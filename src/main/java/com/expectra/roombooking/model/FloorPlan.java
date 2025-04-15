package com.expectra.roombooking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "floor_plan")
@Data
public class FloorPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "floor_plan_id")
    private Long id;

    @Column(name = "building")
    private String building;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "plan_url", nullable = false)
    private String planUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    @JsonBackReference
    private Hotel hotel;
}
