package com.fixmate.service;

import com.fixmate.dto.AssignmentDTO;
import com.fixmate.model.Assignment;
import com.fixmate.model.ServiceRequest;
import com.fixmate.model.User;
import com.fixmate.patterns.adapter.EmailNotificationAdapter;
import com.fixmate.patterns.adapter.EmailService;
import com.fixmate.patterns.adapter.NotificationSender;
import com.fixmate.patterns.command.AssignWorkerCommand;
import com.fixmate.patterns.command.CompleteRequestCommand;
import com.fixmate.patterns.command.RequestInvoker;
import com.fixmate.patterns.mediator.FixMateMediator;
import com.fixmate.patterns.nullobject.NullWorker;
import com.fixmate.patterns.nullobject.RealWorker;
import com.fixmate.patterns.nullobject.Worker;
import com.fixmate.patterns.observer.ClientObserver;
import com.fixmate.patterns.observer.RequestStatusPublisher;
import com.fixmate.patterns.observer.WorkerObserver;
import com.fixmate.patterns.singleton.AppLogger;
import com.fixmate.repository.AssignmentRepository;
import com.fixmate.repository.ServiceRequestRepository;
import com.fixmate.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final UserRepository userRepository;

    // Singleton Pattern: one logger for the whole service
    private final AppLogger logger = AppLogger.getInstance();

    // Observer Pattern: publisher notifies client and worker on status change
    private final RequestStatusPublisher publisher = new RequestStatusPublisher();

    // Adapter Pattern: send notifications via email channel
    private final NotificationSender notificationSender =
            new EmailNotificationAdapter(new EmailService());

    // Mediator Pattern: routes messages between worker, client, admin
    private final FixMateMediator mediator = new FixMateMediator();

    public AssignmentService(AssignmentRepository assignmentRepository,
                             ServiceRequestRepository serviceRequestRepository,
                             UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.serviceRequestRepository = serviceRequestRepository;
        this.userRepository = userRepository;

        // Register observers
        publisher.subscribe(new ClientObserver());
        publisher.subscribe(new WorkerObserver());
    }

    public List<AssignmentDTO> getAllAssignments() {
        return assignmentRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AssignmentDTO getAssignmentById(Long id) {
        return toDTO(assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id)));
    }

    public List<AssignmentDTO> getAssignmentsByWorker(Long workerId) {
        logger.info("Fetching assignments for worker id: " + workerId);
        return assignmentRepository.findByWorkerId(workerId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AssignmentDTO createAssignment(AssignmentDTO dto) {
        ServiceRequest request = serviceRequestRepository.findById(dto.getServiceRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + dto.getServiceRequestId()));
        User workerUser = userRepository.findById(dto.getWorkerId())
                .orElseThrow(() -> new IllegalArgumentException("Worker not found with id: " + dto.getWorkerId()));

        // Guard: only PENDING requests can be assigned
        if (request.getStatus() != ServiceRequest.Status.PENDING) {
            throw new IllegalStateException("Only pending requests can be assigned.");
        }

        // Guard: prevent duplicate assignment
        boolean alreadyAssigned = !assignmentRepository.findByServiceRequestId(request.getId()).isEmpty();
        if (alreadyAssigned) {
            throw new IllegalStateException("This request is already assigned to a worker.");
        }

        // Null Object Pattern: wrap user in Worker interface
        Worker worker = new RealWorker(workerUser.getName(), workerUser.getEmail());

        // Command Pattern: AssignWorkerCommand encapsulates the assign action
        RequestInvoker invoker = new RequestInvoker();
        invoker.executeCommand(new AssignWorkerCommand(
                serviceRequestRepository, assignmentRepository, request, workerUser));

        logger.info("Assignment created — worker: " + worker.getName()
                + ", request: " + request.getId());

        // Observer Pattern: notify subscribers
        publisher.notifyObservers(request.getId(), "ASSIGNED", workerUser.getEmail());

        // Adapter Pattern: send email notification
        notificationSender.send(workerUser.getEmail(),
                "You have been assigned to request #" + request.getId());

        // Mediator Pattern: route system message
        mediator.notifyStatusChange(request.getId(), "ASSIGNED");

        return toDTO(assignmentRepository.findByServiceRequestId(request.getId())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Assignment save failed")));
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    // Called by ServiceRequestService when worker updates status
    public void handleStatusChange(Long requestId, String newStatus, String workerEmail) {
        // Command Pattern: CompleteRequestCommand for COMPLETED status
        if ("COMPLETED".equalsIgnoreCase(newStatus)) {
            RequestInvoker invoker = new RequestInvoker();
            invoker.executeCommand(new CompleteRequestCommand(serviceRequestRepository, requestId));
        }

        logger.info("Status changed to " + newStatus + " for request #" + requestId);

        // Observer Pattern: notify observers
        publisher.notifyObservers(requestId, newStatus, workerEmail);

        // Adapter Pattern: email notification to worker
        notificationSender.send(workerEmail,
                "Request #" + requestId + " status updated to: " + newStatus);

        // Mediator Pattern: route notification to right parties
        mediator.notifyStatusChange(requestId, newStatus);
    }

    private AssignmentDTO toDTO(Assignment a) {
        ServiceRequest req = a.getServiceRequest();

        // Null Object Pattern: use NullWorker if worker is somehow missing
        Worker worker = (a.getWorker() != null)
                ? new RealWorker(a.getWorker().getName(), a.getWorker().getEmail())
                : new NullWorker();

        if (worker.isNull()) {
            logger.warn("Assignment #" + a.getId() + " has no worker — using NullWorker.");
        }

        return AssignmentDTO.builder()
                .id(a.getId())
                .serviceRequestId(req.getId())
                .workerId(worker.isNull() ? null : a.getWorker().getId())
                .workerName(worker.isNull() ? null : worker.getName())
                .assignedDate(a.getAssignedDate())
                // Embedded request details for the worker dashboard
                .address(req.getAddress())
                .bookingDate(req.getBookingDate())
                .bookingTime(req.getBookingTime())
                .issueDescription(req.getIssueDescription())
                .status(req.getStatus())
                .build();
    }
}
