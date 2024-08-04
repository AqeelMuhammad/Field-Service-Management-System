package com.example.field_service_management.service.imp;

import com.example.field_service_management.dto.JobOrderRequest;
import com.example.field_service_management.model.JobOrder;
import com.example.field_service_management.model.Location;
import com.example.field_service_management.repository.JobOrderRepository;
import com.example.field_service_management.service.LocationService;
import com.example.field_service_management.service.JobOrderService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class JobOrderServiceImpl implements JobOrderService {

    private final JobOrderRepository jobOrderRepository;
    private final LocationService  locationService;

    @Autowired
    public JobOrderServiceImpl(JobOrderRepository jobOrderRepository, LocationService locationService) {
        this.jobOrderRepository = jobOrderRepository;
        this.locationService = locationService ;
    }

    @Override
    public JobOrder createJobOrder(JobOrderRequest jobOrderRequest) {
        JobOrder jobOrder = new JobOrder();
        jobOrder.setTitle(jobOrderRequest.getTitle());
        jobOrder.setDescription(jobOrderRequest.getDescription());
        jobOrder.setStatus(jobOrderRequest.getStatus() != null ? jobOrderRequest.getStatus() : JobOrder.Status.PENDING);
        jobOrder.setScheduledDate(jobOrderRequest.getScheduledDate());
        jobOrder.setTechnicianId(jobOrderRequest.getTechnicianId());

        JobOrder createdJobOrder = jobOrderRepository.save(jobOrder);

        Location location = new Location();
        location.setJobOrderId(createdJobOrder.getId());
        location.setLatitude(jobOrderRequest.getLocation().getLatitude());
        location.setLongitude(jobOrderRequest.getLocation().getLongitude());

        locationService.createLocation(location);

        return createdJobOrder;
    }

    @Override
    public Optional<JobOrder> getJobOrderById(UUID id) {
        return jobOrderRepository.findById(id);
    }

    @Override
    public List<JobOrder> getFilteredJobOrders(String title, JobOrder.Status status, LocalDateTime startDate, LocalDateTime endDate, UUID technicianId) {
        return jobOrderRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("scheduledDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("scheduledDate"), endDate));
            }
            if (technicianId != null) {
                predicates.add(criteriaBuilder.equal(root.get("technicianId"), technicianId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }


    @Override
    public List<JobOrder> getCompletedJobsWithinTimeframe(LocalDateTime startDate, LocalDateTime endDate) {
        return jobOrderRepository.findByStatusAndScheduledDateBetween(
                JobOrder.Status.COMPLETED, startDate, endDate
        );
    }

    @Override
    public JobOrder updateJobOrder(UUID id, JobOrderRequest jobOrder) {
        JobOrder existingJobOrder = jobOrderRepository.findById(id).orElse(null);
        if (existingJobOrder == null) {
            return null;
        }

        if (jobOrder.getTitle() != null) {
            existingJobOrder.setTitle(jobOrder.getTitle());
        }
        if (jobOrder.getDescription() != null) {
            existingJobOrder.setDescription(jobOrder.getDescription());
        }
        if (jobOrder.getStatus() != null) {
            existingJobOrder.setStatus(jobOrder.getStatus());
        }
        if (jobOrder.getScheduledDate() != null) {
            existingJobOrder.setScheduledDate(jobOrder.getScheduledDate());
        }
        if (jobOrder.getTechnicianId() != null) {
            existingJobOrder.setTechnicianId(jobOrder.getTechnicianId());
        }

        JobOrder updatedJobOrder = jobOrderRepository.save(existingJobOrder);

        if (jobOrder.getLocation() != null) {
            Location existingLocation = locationService.getLocationByJobOrderId(updatedJobOrder.getId());
            if (existingLocation != null) {
                existingLocation.setLatitude(jobOrder.getLocation().getLatitude());
                existingLocation.setLongitude(jobOrder.getLocation().getLongitude());
                locationService.updateLocation(existingLocation.getId(), existingLocation);
            } else {
                Location newLocation = new Location();
                newLocation.setJobOrderId(updatedJobOrder.getId());
                newLocation.setLatitude(jobOrder.getLocation().getLatitude());
                newLocation.setLongitude(jobOrder.getLocation().getLongitude());
                locationService.createLocation(newLocation);
            }
        }

        return updatedJobOrder;

    }


    @Override
    public JobOrder updateJobOrderStatus(UUID id, JobOrder.Status status) {
        Optional<JobOrder> jobOrderOptional = jobOrderRepository.findById(id);
        if (jobOrderOptional.isPresent()) {
            JobOrder jobOrder = jobOrderOptional.get();
            jobOrder.setStatus(status);
            return jobOrderRepository.save(jobOrder);
        }
        return null;
    }
}
