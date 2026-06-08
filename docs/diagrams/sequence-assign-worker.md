# Sequence Diagram — Admin Assigns a Worker

## Explanation
Shows the admin assigning a worker to a pending request — including validation guards (status check, duplicate check), the Command pattern executing the assignment, and Observer/Adapter/Mediator notifications being fired.

## Mermaid

```mermaid
sequenceDiagram
    actor Admin
    participant Frontend
    participant AssignmentController
    participant AssignmentService
    participant AssignmentRepository
    participant ServiceRequestRepository
    participant UserRepository
    participant Database

    Admin->>Frontend: Select request, pick worker, click Confirm
    Frontend->>AssignmentController: POST /api/assignments {serviceRequestId, workerId}

    AssignmentController->>AssignmentService: createAssignment(dto)

    AssignmentService->>ServiceRequestRepository: findById(serviceRequestId)
    ServiceRequestRepository->>Database: SELECT * FROM service_requests WHERE id=?
    Database-->>ServiceRequestRepository: ServiceRequest
    ServiceRequestRepository-->>AssignmentService: ServiceRequest

    AssignmentService->>AssignmentService: check status == PENDING
    AssignmentService->>AssignmentRepository: findByServiceRequestId(id)
    AssignmentRepository-->>AssignmentService: [] empty — not yet assigned

    AssignmentService->>UserRepository: findById(workerId)
    UserRepository->>Database: SELECT * FROM users WHERE id=?
    Database-->>UserRepository: User (worker)
    UserRepository-->>AssignmentService: Worker User

    AssignmentService->>ServiceRequestRepository: save(request {status=ASSIGNED})
    ServiceRequestRepository->>Database: UPDATE service_requests SET status=ASSIGNED
    AssignmentService->>AssignmentRepository: save(assignment)
    AssignmentRepository->>Database: INSERT INTO assignments

    AssignmentService->>AssignmentService: publisher.notifyObservers() [Observer Pattern]
    AssignmentService->>AssignmentService: notificationSender.send() [Adapter Pattern]
    AssignmentService->>AssignmentService: mediator.notifyStatusChange() [Mediator Pattern]

    AssignmentService-->>AssignmentController: AssignmentDTO
    AssignmentController-->>Frontend: 200 OK {id, workerId, workerName, ...}
    Frontend-->>Admin: Close modal, refresh requests table
```
