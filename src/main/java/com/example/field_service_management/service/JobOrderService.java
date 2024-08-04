package com.example.field_service_management.service;

import com.example.field_service_management.dto.JobOrderRequest;
import com.example.field_service_management.model.JobOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobOrderService {
    JobOrder createJobOrder(JobOrderRequest jobOrder);
    Optional<JobOrder> getJobOrderById(UUID id);
    List<JobOrder> getFilteredJobOrders(String title, JobOrder.Status status, LocalDateTime startDate, LocalDateTime endDate, UUID technicianId);
    JobOrder updateJobOrder(UUID id, JobOrderRequest jobOrder);
    JobOrder updateJobOrderStatus(UUID id, JobOrder.Status status);
    List<JobOrder> getCompletedJobsWithinTimeframe(LocalDateTime startDate, LocalDateTime endDate);
}