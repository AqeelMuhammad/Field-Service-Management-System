package com.example.field_service_management.service;

import com.example.field_service_management.model.Coordinates;

public interface GoogleMapsService {
    String getAddressFromCoordinates(double latitude, double longitude);
    Coordinates getCoordinatesFromAddress(String address);
}
