package com.example.field_service_management.service.imp;

import com.example.field_service_management.config.GoogleMapsConfig;
import com.example.field_service_management.model.Coordinates;
import com.example.field_service_management.model.GeocodingResponse;
import com.example.field_service_management.service.GoogleMapsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GoogleMapsServiceImpl implements GoogleMapsService {

    private final GoogleMapsConfig googleMapsConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public GoogleMapsServiceImpl(GoogleMapsConfig googleMapsConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.googleMapsConfig = googleMapsConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private static final String GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s";
    private static final String REVERSE_GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s";

    @Override
    public String getAddressFromCoordinates(double latitude, double longitude) {
        try {
            String url = String.format(GEOCODE_API_URL, latitude, longitude, googleMapsConfig.getApiKey());
            String response = restTemplate.getForObject(url, String.class);
            GeocodingResponse geocodingResponse = objectMapper.readValue(response, GeocodingResponse.class);
//            JsonNode root = objectMapper.readTree(response);
//            JsonNode location = root.path("results").path(0).path("formatted_address");
//            System.out.print("location : "+location);
            return Optional.ofNullable(geocodingResponse)
                    .map(GeocodingResponse::getResults)
                    .flatMap(results -> results.stream().findFirst())
                    .map(GeocodingResponse.Result::getFormattedAddress)
                    .orElse("Address not found");
        }
        catch (Exception e) {
            System.out.print("Error retrieving address");
            return null;
        }
    }

    @Override
    public Coordinates getCoordinatesFromAddress(String address) {
        try {
            String url = String.format(REVERSE_GEOCODE_API_URL, address, googleMapsConfig.getApiKey());
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode location = root.path("results").path(0).path("geometry").path("location");
            double lat = location.path("lat").asDouble();
            double lng = location.path("lng").asDouble();
            return new Coordinates(lat, lng);
        } catch (Exception e) {
            System.out.print("Error retrieving coordinates");
            return null;
        }
    }


}