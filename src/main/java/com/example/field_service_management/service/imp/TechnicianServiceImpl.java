package com.example.field_service_management.service.imp;

import com.example.field_service_management.dto.TechnicianRequest;
import com.example.field_service_management.model.Technician;
import com.example.field_service_management.repository.TechnicianRepository;
import com.example.field_service_management.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;

    @Autowired
    public TechnicianServiceImpl(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    @Override
    public Technician createTechnician(TechnicianRequest technicianRequest) {
        Technician technician = new Technician();
        technician.setName(technicianRequest.getName());
        technician.setEmail(technicianRequest.getEmail());
        technician.setPhone(technicianRequest.getPhone());
        return technicianRepository.save(technician);
    }

    @Override
    public Optional<Technician> getTechnicianById(UUID id) {
        return technicianRepository.findById(id);
    }

    @Override
    public List<Technician> getAllTechnicians() {
        return technicianRepository.findAll();
    }

    @Override
    public Technician updateTechnician(UUID id, Technician technicianUpdates) {
        Technician existingTechnician = technicianRepository.findById(id).orElse(null);
        if (existingTechnician == null) {
            return null;
        }

        if (technicianUpdates.getName() != null) {
            existingTechnician.setName(technicianUpdates.getName());
        }
        if (technicianUpdates.getEmail() != null) {
            existingTechnician.setEmail(technicianUpdates.getEmail());
        }
        if (technicianUpdates.getPhone() != null) {
            existingTechnician.setPhone(technicianUpdates.getPhone());
        }
        return technicianRepository.save(existingTechnician);
    }
}
