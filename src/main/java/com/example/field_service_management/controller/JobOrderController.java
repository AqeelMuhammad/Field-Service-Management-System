package com.example.field_service_management.controller;

import com.example.field_service_management.dto.JobOrderRequest;
import com.example.field_service_management.model.JobOrder;
import com.example.field_service_management.service.JobOrderService;

import com.example.field_service_management.service.LocationService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/jobs")
public class JobOrderController {

    private final JobOrderService jobOrderService;
    private final LocationService locationService;

    @Autowired
    public JobOrderController(JobOrderService jobOrderService, LocationService locationService) {
        this.locationService = locationService;
        this.jobOrderService = jobOrderService;
    }

    @PostMapping
    public ResponseEntity<JobOrder> createJobOrder(@Valid @RequestBody JobOrderRequest jobOrderRequest) {
        JobOrder createdJobOrder = jobOrderService.createJobOrder(jobOrderRequest);
        return new ResponseEntity<>(createdJobOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOrder> getJobOrderById(@PathVariable UUID id) {
        Optional<JobOrder> jobOrder = jobOrderService.getJobOrderById(id);
        return jobOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<JobOrder>> getFilteredJobOrders(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) JobOrder.Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) UUID technicianId) {

        List<JobOrder> jobOrders = jobOrderService.getFilteredJobOrders(title, status, startDate, endDate, technicianId);
        if (jobOrders.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(jobOrders);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<JobOrder> updateJobOrder(@PathVariable UUID id, @RequestBody JobOrderRequest jobOrder) {
        JobOrder updatedJobOrder = jobOrderService.updateJobOrder(id, jobOrder);
        if (updatedJobOrder != null) {
            return ResponseEntity.ok(updatedJobOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<JobOrder> updateJobOrderStatus(@PathVariable UUID id, @RequestBody String status) {
        JobOrder.Status jobOrderStatus = JobOrder.Status.valueOf(status.trim().toUpperCase());
        JobOrder updatedJobOrder = jobOrderService.updateJobOrderStatus(id, jobOrderStatus);
        if (updatedJobOrder != null) {
            return ResponseEntity.ok(updatedJobOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/location")
    public ResponseEntity<String> getJobLocationDetails(@PathVariable UUID id) {
        String locationAddress = locationService.getAddressByJobOrderId(id);
        if (locationAddress != null) {
            return ResponseEntity.ok(locationAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
