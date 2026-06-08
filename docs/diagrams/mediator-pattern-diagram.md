# Mediator Pattern Diagram

## Explanation
FixMateMediator decouples components by routing status-change notifications centrally. Instead of services calling each other directly, they all go through the mediator, reducing tight coupling across the system.

## Mermaid

```mermaid
classDiagram
    class ServiceMediator {
        <<interface>>
        +notifyStatusChange(requestId: Long, status: String) void
    }

    class FixMateMediator {
        +notifyStatusChange(requestId: Long, status: String) void
    }

    class ServiceRequestMediator {
        +notifyStatusChange(requestId: Long, status: String) void
    }

    class RequestMediator {
        +notifyStatusChange(requestId: Long, status: String) void
    }

    ServiceMediator <|.. FixMateMediator
    ServiceMediator <|.. ServiceRequestMediator
    ServiceMediator <|.. RequestMediator
    FixMateMediator --> ServiceMediator : delegates
```
