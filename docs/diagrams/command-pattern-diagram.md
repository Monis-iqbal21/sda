# Command Pattern Diagram

## Explanation
RequestInvoker executes any Command through a common interface. Concrete commands (CreateRequestCommand, AssignWorkerCommand, CompleteRequestCommand) encapsulate individual actions, keeping business logic self-contained and making actions easy to extend or undo.

## Mermaid

```mermaid
classDiagram
    class Command {
        <<interface>>
        +execute() void
    }

    class CreateRequestCommand {
        -ServiceRequestRepository repo
        -ServiceRequest request
        +execute() void
    }

    class AssignWorkerCommand {
        -ServiceRequestRepository requestRepo
        -AssignmentRepository assignmentRepo
        -ServiceRequest request
        -User worker
        +execute() void
    }

    class CompleteRequestCommand {
        -ServiceRequestRepository repo
        -Long requestId
        +execute() void
    }

    class RequestInvoker {
        +executeCommand(command: Command) void
    }

    Command <|.. CreateRequestCommand
    Command <|.. AssignWorkerCommand
    Command <|.. CompleteRequestCommand
    RequestInvoker --> Command : executes
```
