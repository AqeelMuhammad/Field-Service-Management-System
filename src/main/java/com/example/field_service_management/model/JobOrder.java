package com.example.field_service_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "job_orders")
public class JobOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime scheduledDate;

    @Column(name = "technician_id")
    private UUID technicianId;

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }

}
