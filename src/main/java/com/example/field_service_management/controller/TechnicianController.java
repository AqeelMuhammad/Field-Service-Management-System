package com.example.field_service_management.controller;

import com.example.field_service_management.dto.TechnicianRequest;
import com.example.field_service_management.model.Technician;
import com.example.field_service_management.service.TechnicianService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    @Autowired
    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @PostMapping
    public ResponseEntity<Technician> createTechnician(@Valid @RequestBody TechnicianRequest technicianRequest){
        Technician createdTechnician = technicianService.createTechnician(technicianRequest);
        return new ResponseEntity<>(createdTechnician, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Technician> getTechnicianById(@PathVariable UUID id) {
        Optional<Technician> technician = technicianService.getTechnicianById(id);
        return technician.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Technician> updateTechnician(@PathVariable UUID id, @RequestBody Technician technician) {
        Technician updatedTechnician = technicianService.updateTechnician(id, technician);
        if (updatedTechnician != null) {
            return ResponseEntity.ok(updatedTechnician);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Technician>> getAllTechnicians() {
        List<Technician> technicians = technicianService.getAllTechnicians();
        if (technicians.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(technicians);
        }
    }
}
