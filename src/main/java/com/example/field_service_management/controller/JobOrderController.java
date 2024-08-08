package com.example.field_service_management.controller;

import com.example.field_service_management.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<JobOrder>> createJobOrder(@Valid @RequestBody JobOrderRequest jobOrderRequest) {
        JobOrder createdJobOrder = jobOrderService.createJobOrder(jobOrderRequest);
        ApiResponse<JobOrder> response = new ApiResponse<>("Success", "Create Job Order Success", createdJobOrder);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobOrder>> getJobOrderById(@PathVariable UUID id) {
        Optional<JobOrder> jobOrder = jobOrderService.getJobOrderById(id);
        if (jobOrder.isPresent()) {
            ApiResponse<JobOrder> response = new ApiResponse<>("Success", "Get Job Order Success", jobOrder.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<JobOrder> response = new ApiResponse<>("Fail", "Get Job Order Fail", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobOrder>>> getFilteredJobOrders(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) JobOrder.Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) UUID technicianId) {

        List<JobOrder> jobOrders = jobOrderService.getFilteredJobOrders(title, status, startDate, endDate, technicianId);
        if (jobOrders.isEmpty()) {
            ApiResponse<List<JobOrder>> response = new ApiResponse<>("Fail", "No Job Orders Found", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<List<JobOrder>> response = new ApiResponse<>("Success", "Get Job Orders Success", jobOrders);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobOrder>> updateJobOrder(@PathVariable UUID id, @RequestBody JobOrderRequest jobOrderRequest) {
        JobOrder updatedJobOrder = jobOrderService.updateJobOrder(id, jobOrderRequest);
        if (updatedJobOrder != null) {
            ApiResponse<JobOrder> response = new ApiResponse<>("Success", "Update Job Order Success", updatedJobOrder);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<JobOrder> response = new ApiResponse<>("Fail", "Update Job Order Fail", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<JobOrder>> updateJobOrderStatus(@PathVariable UUID id, @RequestBody String status) {
        JobOrder.Status jobOrderStatus = JobOrder.Status.valueOf(status.trim().toUpperCase());
        JobOrder updatedJobOrder = jobOrderService.updateJobOrderStatus(id, jobOrderStatus);
        if (updatedJobOrder != null) {
            ApiResponse<JobOrder> response = new ApiResponse<>("Success", "Update Job Order Status Success", updatedJobOrder);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<JobOrder> response = new ApiResponse<>("Fail", "Update Job Order Status Fail", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/location")
    public ResponseEntity<ApiResponse<String>> getJobLocationDetails(@PathVariable UUID id) {
        String locationAddress = locationService.getAddressByJobOrderId(id);
        if (locationAddress != null) {
            ApiResponse<String> response = new ApiResponse<>("Success", "Get Job Location Success", locationAddress);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<String> response = new ApiResponse<>("Fail", "Get Job Location Fail", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
