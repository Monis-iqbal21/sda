# Observer Pattern Diagram

## Explanation
RequestStatusPublisher holds a list of StatusObserver subscribers. When a request status changes, it calls update() on all registered observers. ClientObserver notifies the client, WorkerObserver notifies the assigned worker — without the publisher knowing the details of either.

## Mermaid

```mermaid
classDiagram
    class StatusObserver {
        <<interface>>
        +update(requestId: Long, status: String, email: String) void
    }

    class ClientObserver {
        +update(requestId: Long, status: String, email: String) void
    }

    class WorkerObserver {
        +update(requestId: Long, status: String, email: String) void
    }

    class RequestStatusPublisher {
        -List~StatusObserver~ observers
        +subscribe(observer: StatusObserver) void
        +notifyObservers(requestId: Long, status: String, email: String) void
    }

    StatusObserver <|.. ClientObserver
    StatusObserver <|.. WorkerObserver
    RequestStatusPublisher --> StatusObserver : notifies all
```
