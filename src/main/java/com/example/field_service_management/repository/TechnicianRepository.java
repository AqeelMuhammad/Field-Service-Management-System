package com.example.field_service_management.repository;

import com.example.field_service_management.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, UUID> {
}