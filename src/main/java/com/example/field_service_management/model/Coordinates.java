package com.example.field_service_management.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Coordinates {
    private double latitude;
    private double longitude;

    public Coordinates() {}

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("Latitude: %f, Longitude: %f", latitude, longitude);
    }

}
