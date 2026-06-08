package com.fixmate.service;

import com.fixmate.dto.ServiceRequestDTO;
import com.fixmate.model.ServiceEntity;
import com.fixmate.model.ServiceRequest;
import com.fixmate.model.User;
import com.fixmate.repository.AssignmentRepository;
import com.fixmate.repository.ServiceRepository;
import com.fixmate.repository.ServiceRequestRepository;
import com.fixmate.repository.UserRepository;
import com.fixmate.patterns.singleton.AppLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentService assignmentService;

    public ServiceRequestService(ServiceRequestRepository serviceRequestRepository,
                                 UserRepository userRepository,
                                 ServiceRepository serviceRepository,
                                 AssignmentRepository assignmentRepository,
                                 @Lazy AssignmentService assignmentService) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.assignmentRepository = assignmentRepository;
        this.assignmentService = assignmentService;
    }

    // Singleton Pattern: application-wide logger
    private final AppLogger logger = AppLogger.getInstance();

    public List<ServiceRequestDTO> getAllRequests() {
        return serviceRequestRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ServiceRequestDTO getRequestById(Long id) {
        return toDTO(serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceRequest not found with id: " + id)));
    }

    public List<ServiceRequestDTO> getRequestsByClient(Long clientId) {
        return serviceRequestRepository.findByClientId(clientId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ServiceRequestDTO createRequest(ServiceRequestDTO dto) {
        User client = userRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        ServiceEntity service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found"));

        ServiceRequest request = ServiceRequest.builder()
                .client(client)
                .service(service)
                .address(dto.getAddress())
                .bookingDate(dto.getBookingDate())
                .bookingTime(dto.getBookingTime())
                .issueDescription(dto.getIssueDescription())
                .status(ServiceRequest.Status.PENDING)
                .build();

        logger.info("Service request created for client: " + client.getEmail());
        return toDTO(serviceRequestRepository.save(request));
    }

    public ServiceRequestDTO updateStatus(Long id, ServiceRequest.Status status) {
        ServiceRequest request = serviceRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceRequest not found with id: " + id));

        request.setStatus(status);
        ServiceRequest saved = serviceRequestRepository.save(request);

        // Resolve the assigned worker's email for notifications (if any)
        String workerEmail = assignmentRepository.findByServiceRequestId(id)
                .stream()
                .findFirst()
                .map(a -> a.getWorker().getEmail())
                .orElse("unknown@fixmate.com");

        // Delegate to AssignmentService which wires Command, Observer, Adapter, Mediator
        assignmentService.handleStatusChange(id, status.name(), workerEmail);

        return toDTO(saved);
    }

    public void deleteRequest(Long id) {
        serviceRequestRepository.deleteById(id);
    }

    private ServiceRequestDTO toDTO(ServiceRequest r) {
        return ServiceRequestDTO.builder()
                .id(r.getId())
                .clientId(r.getClient().getId())
                .serviceId(r.getService().getId())
                .address(r.getAddress())
                .bookingDate(r.getBookingDate())
                .bookingTime(r.getBookingTime())
                .issueDescription(r.getIssueDescription())
                .status(r.getStatus())
                .build();
    }
}
