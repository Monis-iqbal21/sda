package com.fixmate.patterns.command;

import com.fixmate.model.ServiceRequest;
import com.fixmate.repository.ServiceRequestRepository;

public class UpdateRequestStatusCommand implements Command {

    private final ServiceRequestRepository repository;
    private final Long requestId;
    private final ServiceRequest.Status newStatus;

    public UpdateRequestStatusCommand(ServiceRequestRepository repository,
                                      Long requestId,
                                      ServiceRequest.Status newStatus) {
        this.repository = repository;
        this.requestId = requestId;
        this.newStatus = newStatus;
    }

    @Override
    public void execute() {
        repository.findById(requestId).ifPresent(r -> {
            r.setStatus(newStatus);
            repository.save(r);
        });
    }
}
