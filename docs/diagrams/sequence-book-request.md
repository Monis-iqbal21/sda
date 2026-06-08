# Sequence Diagram — Client Books a Service

## Explanation
Shows the full flow when a client submits a booking — from the frontend form through the controller, service, repository, and database, returning a ServiceRequestDTO with PENDING status.

## Mermaid

```mermaid
sequenceDiagram
    actor Client
    participant Frontend
    participant ServiceRequestController
    participant ServiceRequestService
    participant UserRepository
    participant ServiceRepository
    participant ServiceRequestRepository
    participant Database

    Client->>Frontend: Fill booking form & submit
    Frontend->>ServiceRequestController: POST /api/requests {clientId, serviceId, address, bookingDate, bookingTime, issueDescription}

    ServiceRequestController->>ServiceRequestService: createRequest(dto)

    ServiceRequestService->>UserRepository: findById(clientId)
    UserRepository->>Database: SELECT * FROM users WHERE id=?
    Database-->>UserRepository: User
    UserRepository-->>ServiceRequestService: User (client)

    ServiceRequestService->>ServiceRepository: findById(serviceId)
    ServiceRepository->>Database: SELECT * FROM services WHERE id=?
    Database-->>ServiceRepository: ServiceEntity
    ServiceRepository-->>ServiceRequestService: ServiceEntity

    ServiceRequestService->>ServiceRequestRepository: save(serviceRequest {status=PENDING})
    ServiceRequestRepository->>Database: INSERT INTO service_requests
    Database-->>ServiceRequestRepository: saved ServiceRequest
    ServiceRequestRepository-->>ServiceRequestService: ServiceRequest

    ServiceRequestService-->>ServiceRequestController: ServiceRequestDTO
    ServiceRequestController-->>Frontend: 200 OK {id, status: PENDING, ...}
    Frontend-->>Client: Show success / redirect to My Requests
```
