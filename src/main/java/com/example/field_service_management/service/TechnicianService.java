package com.example.field_service_management.service;

import com.example.field_service_management.dto.TechnicianRequest;
import com.example.field_service_management.model.Technician;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TechnicianService {
    Technician createTechnician(TechnicianRequest technician);
    Optional<Technician> getTechnicianById(UUID id);
    List<Technician> getAllTechnicians();
    Technician updateTechnician(UUID id, Technician technician);
}