# Factory Pattern Diagram

## Explanation
ServiceFactory creates concrete service objects based on a type string. All concrete services implement the HomeService interface. This decouples the creation logic from the caller, allowing new service types to be added without changing existing code.

## Mermaid

```mermaid
classDiagram
    class HomeService {
        <<interface>>
        +execute() void
        +getServiceName() String
    }

    class PlumbingService {
        +execute() void
        +getServiceName() String
    }

    class ElectricalService {
        +execute() void
        +getServiceName() String
    }

    class CleaningService {
        +execute() void
        +getServiceName() String
    }

    class ServiceFactory {
        +createService(type: String) HomeService
    }

    HomeService <|.. PlumbingService
    HomeService <|.. ElectricalService
    HomeService <|.. CleaningService
    ServiceFactory --> HomeService : creates
```
