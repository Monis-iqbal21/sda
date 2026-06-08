# Use Case Diagram

## Explanation
Shows what each actor (Client, Admin, Worker) can do in the FixMate system. Client focuses on booking and tracking, Admin on management and assignment, Worker on job execution.

## Mermaid

```mermaid
graph TB
    subgraph FixMate System
        UC1([Register])
        UC2([Login])
        UC3([Book Service])
        UC4([View My Requests])
        UC5([Track Status])
        UC6([View All Requests])
        UC7([View Workers])
        UC8([Assign Worker])
        UC9([Monitor Status])
        UC10([View Assigned Jobs])
        UC11([Start Work])
        UC12([Complete Work])
    end

    Client((Client))
    Admin((Admin))
    Worker((Worker))

    Client --> UC1
    Client --> UC2
    Client --> UC3
    Client --> UC4
    Client --> UC5

    Admin --> UC2
    Admin --> UC6
    Admin --> UC7
    Admin --> UC8
    Admin --> UC9

    Worker --> UC2
    Worker --> UC10
    Worker --> UC11
    Worker --> UC12
```
