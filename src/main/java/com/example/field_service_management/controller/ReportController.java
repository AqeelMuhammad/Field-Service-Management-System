package com.example.field_service_management.controller;

import com.example.field_service_management.model.JobOrder;
import com.example.field_service_management.service.JobOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<List<JobOrder>> getCompletedJobOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<JobOrder> completedJobs = jobOrderService.getCompletedJobsWithinTimeframe(startDate, endDate);
        if (completedJobs.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(completedJobs);
        }
    }
}

