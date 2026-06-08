# Singleton Pattern Diagram

## Explanation
AppLogger uses the Singleton pattern — only one instance exists across the entire application. All services access the same logger via getInstance(), ensuring consistent logging without creating multiple objects.

## Mermaid

```mermaid
classDiagram
    class AppLogger {
        -static AppLogger instance
        -AppLogger()
        +static getInstance() AppLogger
        +info(message: String) void
        +warn(message: String) void
        +error(message: String) void
    }

    AppLogger --> AppLogger : getInstance() returns single instance
```
