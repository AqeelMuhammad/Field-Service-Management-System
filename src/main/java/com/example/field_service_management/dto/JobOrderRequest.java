package com.example.field_service_management.dto;

import com.example.field_service_management.model.JobOrder.Status;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class JobOrderRequest {

    @NotEmpty(message = "Title must not be empty")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    private String description;

    private Status status;

    @NotNull(message = "Scheduled Date must not be null")
    @Future(message = "Scheduled Date must be in the future")
    private LocalDateTime scheduledDate;

    @NotNull(message = "Technician ID must not be null")
    private UUID technicianId;

    @NotNull(message = "Location must not be empty")
    private LocationRequest location;

    @Getter
    @Setter
    @NotNull
    public static class LocationRequest {

        @NotNull(message = "Latitude must not be null")
        @Min(value = 0, message = "Latitude must be between 0 and 90")
        @Max(value = 90, message = "Latitude must be between 0 and 90")
        private Double latitude;

        @NotNull(message = "Longitude must not be null")
        @PositiveOrZero(message = "Longitude must be zero or positive")
        private Double longitude;
    }
}
