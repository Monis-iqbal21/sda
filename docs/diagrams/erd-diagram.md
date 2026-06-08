# Entity Relationship Diagram (ERD)

## Explanation
Shows all database tables, their primary keys, field types, and foreign key relationships. service_requests is the central table linked to users (as client) and services. assignments links service_requests to users (as worker).

## Mermaid

```mermaid
erDiagram
    users {
        BIGINT id PK
        VARCHAR name
        VARCHAR email
        VARCHAR password
        VARCHAR role
    }

    services {
        BIGINT id PK
        VARCHAR name
        TEXT description
    }

    service_requests {
        BIGINT id PK
        BIGINT client_id FK
        BIGINT service_id FK
        VARCHAR address
        DATE booking_date
        TIME booking_time
        TEXT issue_description
        VARCHAR status
    }

    assignments {
        BIGINT id PK
        BIGINT service_request_id FK
        BIGINT worker_id FK
        DATE assigned_date
    }

    notifications {
        BIGINT id PK
        BIGINT user_id FK
        TEXT message
    }

    users ||--o{ service_requests : "creates as client"
    services ||--o{ service_requests : "belongs to"
    service_requests ||--o| assignments : "has one"
    users ||--o{ assignments : "assigned as worker"
    users ||--o{ notifications : "receives"
```
