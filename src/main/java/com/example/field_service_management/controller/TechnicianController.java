package com.example.field_service_management.controller;

import com.example.field_service_management.dto.ApiResponse;
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
    public ResponseEntity createTechnician(@Valid @RequestBody TechnicianRequest technicianRequest){
        Technician createdTechnician = technicianService.createTechnician(technicianRequest);
        ApiResponse response = new ApiResponse("Success", "Create Technician Success", createdTechnician);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Technician>> getTechnicianById(@PathVariable UUID id) {
        Optional<Technician> technician = technicianService.getTechnicianById(id);
        if (technician.isPresent()) {
            ApiResponse<Technician> response = new ApiResponse<>("Success", "Get Technician Success", technician.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Technician> response = new ApiResponse<>("Fail", "Get Technician Fail", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Technician>> updateTechnician(@PathVariable UUID id, @RequestBody Technician technician) {
        Technician updatedTechnician = technicianService.updateTechnician(id, technician);
        if (updatedTechnician != null) {
            ApiResponse<Technician> response = new ApiResponse<>("Success", "Update Technician Success", updatedTechnician);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Technician> response = new ApiResponse<>("Fail", "Update Technician Fail", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Technician>>> getAllTechnicians() {
        List<Technician> technicians = technicianService.getAllTechnicians();
        if (technicians.isEmpty()) {
            ApiResponse<List<Technician>> response = new ApiResponse<>("Fail", "No Technician Found", null);
            return new ResponseEntity(response, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<List<Technician>> response = new ApiResponse<>("Success", "Get Technician Success", technicians);
            return ResponseEntity.ok(response);
        }
    }
}
