package com.fixmate.patterns.command;

import com.fixmate.model.ServiceEntity;
import com.fixmate.model.ServiceRequest;
import com.fixmate.model.User;
import com.fixmate.repository.ServiceRequestRepository;

// Command Pattern - Concrete Command
// Encapsulates the action of creating a new service request.
public class CreateRequestCommand implements Command {

    private final ServiceRequestRepository repository;
    private final User client;
    private final ServiceEntity service;
    private final String address;
    private final String issueDescription;

    public CreateRequestCommand(ServiceRequestRepository repository,
                                User client,
                                ServiceEntity service,
                                String address,
                                String issueDescription) {
        this.repository = repository;
        this.client = client;
        this.service = service;
        this.address = address;
        this.issueDescription = issueDescription;
    }

    @Override
    public void execute() {
        ServiceRequest request = ServiceRequest.builder()
                .client(client)
                .service(service)
                .address(address)
                .issueDescription(issueDescription)
                .status(ServiceRequest.Status.PENDING)
                .build();
        repository.save(request);
        System.out.println("[CreateRequestCommand] Service request created for client: " + client.getEmail());
    }
}
