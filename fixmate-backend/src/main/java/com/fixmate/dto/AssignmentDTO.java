package com.fixmate.dto;

import com.fixmate.model.ServiceRequest;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AssignmentDTO {
    // Assignment fields
    private Long id;
    private Long serviceRequestId;
    private Long workerId;
    private String workerName;
    private LocalDate assignedDate;

    // Embedded request details for the worker dashboard
    private String address;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private String issueDescription;
    private ServiceRequest.Status status;
}
