package com.example.field_service_management.service.imp;

import com.example.field_service_management.service.GoogleMapsService;
import com.example.field_service_management.model.Location;
import com.example.field_service_management.repository.LocationRepository;
import com.example.field_service_management.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final GoogleMapsService  googleMapsService;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, GoogleMapsService  googleMapsService) {
        this.locationRepository = locationRepository;
        this.googleMapsService = googleMapsService;
    }

    @Override
    public void createLocation(Location location) {
        locationRepository.save(location);
    }

    @Override
    public Location getLocationByJobOrderId(UUID jobOrderId) {
        return locationRepository.findByJobOrderId(jobOrderId);
    }

    public String getAddressByJobOrderId(UUID jobOrderId) {
        Location locationResponse = this.getLocationByJobOrderId(jobOrderId);
        if (locationResponse==null){
            return null;
        }
        return googleMapsService.getAddressFromCoordinates(locationResponse.getLatitude(), locationResponse.getLongitude());
    }

    @Override
    public void updateLocation(UUID id, Location location) {
        location.setId(id);
        locationRepository.save(location);
    }
}
