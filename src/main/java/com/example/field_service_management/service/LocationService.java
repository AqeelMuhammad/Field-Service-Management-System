package com.example.field_service_management.service;

import com.example.field_service_management.model.Location;

import java.util.UUID;

public interface LocationService {
    void createLocation(Location location);
    void updateLocation(UUID id, Location location);
    Location getLocationByJobOrderId(UUID id);
    String getAddressByJobOrderId(UUID id);
}