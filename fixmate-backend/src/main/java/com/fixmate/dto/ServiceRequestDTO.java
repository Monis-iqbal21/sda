package com.fixmate.dto;

import com.fixmate.model.ServiceRequest;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceRequestDTO {
    private Long id;
    private Long clientId;
    private Long serviceId;
    private String address;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private String issueDescription;
    private ServiceRequest.Status status;
}
