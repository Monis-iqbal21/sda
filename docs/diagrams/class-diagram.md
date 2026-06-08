# Class Diagram

## Explanation
Shows all domain entities, their fields, and relationships. ServiceRequest is the central entity linking User (as client), ServiceEntity, and Assignment.

## Mermaid

```mermaid
classDiagram
    class User {
        +Long id
        +String name
        +String email
        +String password
        +Role role
    }

    class ServiceEntity {
        +Long id
        +String name
        +String description
    }

    class ServiceRequest {
        +Long id
        +String address
        +LocalDate bookingDate
        +LocalTime bookingTime
        +String issueDescription
        +Status status
    }

    class Assignment {
        +Long id
        +LocalDate assignedDate
    }

    class Notification {
        +Long id
        +String message
    }

    class Role {
        <<enumeration>>
        CLIENT
        ADMIN
        WORKER
    }

    class Status {
        <<enumeration>>
        PENDING
        ASSIGNED
        IN_PROGRESS
        COMPLETED
        CANCELLED
    }

    User --> Role
    ServiceRequest --> Status
    ServiceRequest "many" --> "1" User : client
    ServiceRequest "many" --> "1" ServiceEntity : service
    Assignment "1" --> "1" ServiceRequest : serviceRequest
    Assignment "many" --> "1" User : worker
    Notification "many" --> "1" User : user
```
