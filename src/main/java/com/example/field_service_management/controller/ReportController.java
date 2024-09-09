package com.example.field_service_management.controller;

import com.example.field_service_management.dto.ApiResponse;
import com.example.field_service_management.model.JobOrder;
import com.example.field_service_management.service.JobOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final JobOrderService jobOrderService;

    @Autowired
    public ReportController(JobOrderService jobOrderService) {
        this.jobOrderService = jobOrderService;
    }

    @GetMapping("/completed-jobs")
    public ResponseEntity<ApiResponse<List<JobOrder>>> getCompletedJobOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<JobOrder> completedJobs = jobOrderService.getCompletedJobsWithinTimeframe(startDate, endDate);
        if (completedJobs.isEmpty()) {
            ApiResponse<List<JobOrder>> response = new ApiResponse<>("Fail", "No Completed Jobs Found", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            ApiResponse<List<JobOrder>> response = new ApiResponse<>("Success", "Get Completed Job Orders Success", completedJobs);
            return ResponseEntity.ok(response);
        }
    }
}

