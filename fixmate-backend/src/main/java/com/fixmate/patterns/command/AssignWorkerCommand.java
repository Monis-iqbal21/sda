package com.fixmate.patterns.command;

import com.fixmate.model.Assignment;
import com.fixmate.model.ServiceRequest;
import com.fixmate.model.User;
import com.fixmate.repository.AssignmentRepository;
import com.fixmate.repository.ServiceRequestRepository;

import java.time.LocalDate;

// Command Pattern - Concrete Command
// Encapsulates the action of assigning a worker to a request.
public class AssignWorkerCommand implements Command {

    private final ServiceRequestRepository requestRepository;
    private final AssignmentRepository assignmentRepository;
    private final ServiceRequest request;
    private final User worker;

    public AssignWorkerCommand(ServiceRequestRepository requestRepository,
                               AssignmentRepository assignmentRepository,
                               ServiceRequest request,
                               User worker) {
        this.requestRepository = requestRepository;
        this.assignmentRepository = assignmentRepository;
        this.request = request;
        this.worker = worker;
    }

    @Override
    public void execute() {
        request.setStatus(ServiceRequest.Status.ASSIGNED);
        requestRepository.save(request);

        Assignment assignment = Assignment.builder()
                .serviceRequest(request)
                .worker(worker)
                .assignedDate(LocalDate.now())
                .build();
        assignmentRepository.save(assignment);

        System.out.println("[AssignWorkerCommand] Worker " + worker.getEmail()
                + " assigned to request #" + request.getId());
    }
}
