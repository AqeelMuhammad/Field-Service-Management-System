package com.example.field_service_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "job_order_id")
    private UUID jobOrderId;

    @NotNull(message = "Latitude must not be null")
    private Double latitude;

    @NotNull(message = "Longitude must not be null")
    private Double longitude;
}