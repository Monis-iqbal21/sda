# Activity Diagram — Full FixMate Workflow

## Explanation
Shows the complete end-to-end lifecycle of a service request from client login through job completion, including all status transitions and decision points.

## Mermaid

```mermaid
flowchart TD
    A([Start]) --> B[Client Logs In]
    B --> C[Client Creates Service Request]
    C --> D[Status: PENDING]
    D --> E{Admin Reviews Request}
    E -->|No workers available| E
    E -->|Assigns Worker| F[Status: ASSIGNED]
    F --> G[Worker Views Assigned Job]
    G --> H{Worker Ready to Start?}
    H -->|Not yet| H
    H -->|Clicks Start Work| I[Status: IN_PROGRESS]
    I --> J[Worker Performs Service]
    J --> K[Worker Clicks Mark Completed]
    K --> L[Status: COMPLETED]
    L --> M[Client sees COMPLETED on My Requests]
    L --> N[Admin sees COMPLETED on All Requests]
    M --> O([End])
    N --> O
```
