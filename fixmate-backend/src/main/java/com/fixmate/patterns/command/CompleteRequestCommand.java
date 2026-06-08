package com.fixmate.patterns.command;

import com.fixmate.model.ServiceRequest;
import com.fixmate.repository.ServiceRequestRepository;

// Command Pattern - Concrete Command
// Encapsulates the action of marking a request as COMPLETED.
public class CompleteRequestCommand implements Command {

    private final ServiceRequestRepository repository;
    private final Long requestId;

    public CompleteRequestCommand(ServiceRequestRepository repository, Long requestId) {
        this.repository = repository;
        this.requestId = requestId;
    }

    @Override
    public void execute() {
        repository.findById(requestId).ifPresent(r -> {
            r.setStatus(ServiceRequest.Status.COMPLETED);
            repository.save(r);
            System.out.println("[CompleteRequestCommand] Request #" + requestId + " marked as COMPLETED.");
        });
    }
}
