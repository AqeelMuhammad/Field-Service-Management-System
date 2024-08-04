package com.example.field_service_management.repository;

import com.example.field_service_management.model.JobOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface JobOrderRepository extends JpaRepository<JobOrder, UUID>, JpaSpecificationExecutor<JobOrder> {
    List<JobOrder> findByStatusAndScheduledDateBetween(
            JobOrder.Status status,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}

