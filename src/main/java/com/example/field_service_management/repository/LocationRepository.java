package com.example.field_service_management.repository;

import com.example.field_service_management.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    Location findByJobOrderId(UUID jobOrderId);
}
