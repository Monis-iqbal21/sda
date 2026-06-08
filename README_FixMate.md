# FixMate - Home Service Management System

FixMate is a home service booking system built for an SDA semester project.  
The system allows clients to book home services, admins to assign workers, and workers to update job progress.

The main purpose of this project is to demonstrate a complete working web application with **7 software design patterns** implemented in the Java Spring Boot backend.

---

## Tech Stack

### Frontend
- Next.js
- React.js
- Axios
- Custom CSS / Tailwind-style utility classes
- Orange and white theme

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

### Database
- PostgreSQL

---

## Main Project Flow

```text
Client registers/logs in
        ↓
Client books a service
        ↓
Request status = PENDING
        ↓
Admin sees all requests
        ↓
Admin assigns a worker
        ↓
Request status = ASSIGNED
        ↓
Worker sees assigned job
        ↓
Worker clicks Start Work
        ↓
Request status = IN_PROGRESS
        ↓
Worker clicks Mark Completed
        ↓
Request status = COMPLETED
        ↓
Client and Admin can track final status
```

---

## User Roles

### 1. Client
Client can:
- Register and login
- Book a service using a 3-step form
- View their own requests
- Track request status

Client cannot:
- Select worker
- See worker details

### 2. Admin
Admin can:
- View all service requests
- View all workers
- Assign worker to pending requests
- Track request status

### 3. Worker
Worker can:
- Login
- View jobs assigned to them
- Mark job as `IN_PROGRESS`
- Mark job as `COMPLETED`

---

## Main Backend Packages

```text
com.fixmate
├── controller
├── service
├── repository
├── model
├── dto
├── config
└── patterns
    ├── factory
    ├── command
    ├── nullobject
    ├── mediator
    ├── observer
    ├── singleton
    └── adapter
```

---

## Main Entities / Models

### User
Represents all system users.

Roles:
```text
CLIENT
ADMIN
WORKER
```

Important fields:
```text
id
name
email
password
role
```

### ServiceEntity
Represents available home services.

Examples:
```text
Plumbing
Electrical
Cleaning
```

Important fields:
```text
id
name
description
```

### ServiceRequest
Represents a booking created by a client.

Important fields:
```text
id
client
service
address
bookingDate
bookingTime
issueDescription
status
```

Status values:
```text
PENDING
ASSIGNED
IN_PROGRESS
COMPLETED
CANCELLED
```

### Assignment
Represents worker assignment for a service request.

Important fields:
```text
id
serviceRequest
worker
assignedDate
```

### Notification
Represents notification messages.

Important fields:
```text
id
user
message
isRead
```

---

# Design Patterns Implemented

The project implements 7 design patterns:

```text
1. Factory Pattern
2. Command Pattern
3. Null Object Pattern
4. Mediator Pattern
5. Observer Pattern
6. Singleton Pattern
7. Adapter Pattern
```

All pattern code is mainly inside:

```text
fixmate-backend/src/main/java/com/fixmate/patterns
```

---

# 1. Factory Pattern

## Location

```text
com.fixmate.patterns.factory
```

## Files

```text
HomeService.java
PlumbingService.java
ElectricalService.java
CleaningService.java
ServiceFactory.java
```

## Purpose

Factory Pattern is used when object creation should be handled by a separate factory class instead of directly using `new` everywhere.

In FixMate, different service types exist:

```text
Plumbing
Electrical
Cleaning
```

Instead of creating each service manually, the system can ask the factory to create the correct service object.

## Code Logic

### Product Interface

```java
public interface HomeService {
    String getServiceName();
    String getDescription();
    void execute();
}
```

This interface defines common behavior for all service types.

### Concrete Products

Example:

```java
public class PlumbingService implements HomeService {
    @Override
    public String getServiceName() {
        return "Plumbing";
    }

    @Override
    public String getDescription() {
        return "Pipe and water leakage repair service.";
    }

    @Override
    public void execute() {
        System.out.println("Executing plumbing service.");
    }
}
```

Similar classes exist for:

```text
ElectricalService
CleaningService
```

### Factory Class

```java
public class ServiceFactory {
    public static HomeService create(String serviceType) {
        return switch (serviceType.toUpperCase()) {
            case "PLUMBING" -> new PlumbingService();
            case "ELECTRICAL" -> new ElectricalService();
            case "CLEANING" -> new CleaningService();
            default -> throw new IllegalArgumentException("Invalid service type");
        };
    }
}
```

## How It Works

If the system needs a plumbing service:

```java
HomeService service = ServiceFactory.create("Plumbing");
```

The caller does not need to know which class should be created.  
The factory handles that decision.

## Viva Explanation

Factory Pattern centralizes object creation.  
In FixMate, it creates service objects like Plumbing, Electrical, and Cleaning.  
This makes the code flexible because new service types can be added without changing the main business logic.

---

# 2. Command Pattern

## Location

```text
com.fixmate.patterns.command
```

## Files

```text
Command.java
CreateRequestCommand.java
AssignWorkerCommand.java
CompleteRequestCommand.java
RequestInvoker.java
```

## Purpose

Command Pattern converts an action into an object.

In FixMate, important actions are:

```text
Create request
Assign worker
Complete request
```

Instead of writing all logic directly inside service classes, these actions are placed inside separate command classes.

## Code Logic

### Command Interface

```java
public interface Command {
    void execute();
}
```

Every command must implement the `execute()` method.

### AssignWorkerCommand

This command handles assigning a worker to a request.

General logic:

```java
public class AssignWorkerCommand implements Command {
    private final ServiceRequestRepository serviceRequestRepository;
    private final AssignmentRepository assignmentRepository;
    private final ServiceRequest request;
    private final User worker;

    @Override
    public void execute() {
        Assignment assignment = Assignment.builder()
                .serviceRequest(request)
                .worker(worker)
                .assignedDate(LocalDate.now())
                .build();

        request.setStatus(ServiceRequest.Status.ASSIGNED);
        serviceRequestRepository.save(request);
        assignmentRepository.save(assignment);
    }
}
```

## RequestInvoker

```java
public class RequestInvoker {
    private final List<Command> history = new ArrayList<>();

    public void executeCommand(Command command) {
        command.execute();
        history.add(command);
    }
}
```

The invoker executes commands without knowing their internal logic.

## Used In

```text
AssignmentService.java
ServiceRequestService.java
```

Example:

```java
RequestInvoker invoker = new RequestInvoker();
invoker.executeCommand(new AssignWorkerCommand(...));
```

## How It Works

When admin assigns a worker:

```text
Admin clicks Assign Worker
        ↓
AssignmentService creates AssignWorkerCommand
        ↓
RequestInvoker executes command
        ↓
Assignment is created
        ↓
Request status becomes ASSIGNED
```

## Viva Explanation

Command Pattern encapsulates actions as separate objects.  
In FixMate, assigning a worker and completing a request are implemented as commands.  
This keeps the service classes cleaner and makes actions reusable.

---

# 3. Null Object Pattern

## Location

```text
com.fixmate.patterns.nullobject
```

## Files

```text
Worker.java
RealWorker.java
NullWorker.java
```

## Purpose

Null Object Pattern avoids returning `null`.

Instead of checking:

```java
if (worker != null)
```

the system returns a default object called `NullWorker`.

## Code Logic

### Worker Interface

```java
public interface Worker {
    String getName();
    String getEmail();
    boolean isNull();
    void performWork(String taskDescription);
}
```

### RealWorker

```java
public class RealWorker implements Worker {
    private final String name;
    private final String email;

    public boolean isNull() {
        return false;
    }

    public void performWork(String taskDescription) {
        System.out.println(name + " is working on " + taskDescription);
    }
}
```

### NullWorker

```java
public class NullWorker implements Worker {
    public String getName() {
        return "Unassigned";
    }

    public String getEmail() {
        return "no-worker@fixmate.com";
    }

    public boolean isNull() {
        return true;
    }

    public void performWork(String taskDescription) {
        System.out.println("No worker assigned yet.");
    }
}
```

## Used In

```text
AssignmentService.java
```

Example logic:

```java
Worker worker = (assignment.getWorker() != null)
        ? new RealWorker(assignment.getWorker().getName(), assignment.getWorker().getEmail())
        : new NullWorker();
```

## How It Works

If a request has a worker:

```text
RealWorker is returned
```

If a request does not have a worker:

```text
NullWorker is returned
```

This avoids application crashes.

## Viva Explanation

Null Object Pattern prevents null pointer errors.  
In FixMate, if no worker is assigned, we return `NullWorker` instead of `null`.  
This makes the code safer because methods like `getName()` can still be called.

---

# 4. Mediator Pattern

## Location

```text
com.fixmate.patterns.mediator
```

## Files

```text
ServiceMediator.java
FixMateMediator.java
```

## Purpose

Mediator Pattern controls communication between different objects.

In FixMate:

```text
Client
Admin
Worker
```

should not communicate directly in code.  
Instead, communication is routed through a mediator.

## Code Logic

### Mediator Interface

```java
public interface ServiceMediator {
    void sendMessage(String from, String to, String message);
    void notifyStatusChange(Long requestId, String status);
}
```

### Concrete Mediator

```java
public class FixMateMediator implements ServiceMediator {
    @Override
    public void sendMessage(String from, String to, String message) {
        System.out.println("[Mediator] From " + from + " to " + to + ": " + message);
    }

    @Override
    public void notifyStatusChange(Long requestId, String status) {
        System.out.println("[Mediator] Request #" + requestId + " status changed to " + status);
    }
}
```

## Used In

```text
AssignmentService.java
```

Example:

```java
mediator.notifyStatusChange(request.getId(), "ASSIGNED");
```

## How It Works

When admin assigns a worker:

```text
AssignmentService
        ↓
FixMateMediator
        ↓
Routes message/status update
```

## Viva Explanation

Mediator Pattern reduces direct dependency between objects.  
In FixMate, client, admin, and worker communication is represented through `FixMateMediator`.  
This makes communication centralized and easier to manage.

---

# 5. Observer Pattern

## Location

```text
com.fixmate.patterns.observer
```

## Files

```text
StatusObserver.java
ClientObserver.java
WorkerObserver.java
RequestStatusPublisher.java
```

## Purpose

Observer Pattern is used when one object changes and other objects need to be notified automatically.

In FixMate, when request status changes:

```text
ASSIGNED
IN_PROGRESS
COMPLETED
```

the client and worker observers can be notified.

## Code Logic

### Observer Interface

```java
public interface StatusObserver {
    void onStatusChanged(Long requestId, String newStatus, String targetEmail);
}
```

### Client Observer

```java
public class ClientObserver implements StatusObserver {
    @Override
    public void onStatusChanged(Long requestId, String newStatus, String targetEmail) {
        System.out.println("Client notified: Request #" + requestId + " is now " + newStatus);
    }
}
```

### Worker Observer

```java
public class WorkerObserver implements StatusObserver {
    @Override
    public void onStatusChanged(Long requestId, String newStatus, String targetEmail) {
        System.out.println("Worker notified: Request #" + requestId + " is now " + newStatus);
    }
}
```

### Publisher / Subject

```java
public class RequestStatusPublisher {
    private final List<StatusObserver> observers = new ArrayList<>();

    public void subscribe(StatusObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(Long requestId, String newStatus, String targetEmail) {
        for (StatusObserver observer : observers) {
            observer.onStatusChanged(requestId, newStatus, targetEmail);
        }
    }
}
```

## Used In

```text
AssignmentService.java
```

Example:

```java
publisher.subscribe(new ClientObserver());
publisher.subscribe(new WorkerObserver());

publisher.notifyObservers(requestId, newStatus, workerEmail);
```

## How It Works

```text
Request status changes
        ↓
RequestStatusPublisher notifies observers
        ↓
ClientObserver and WorkerObserver react
```

## Viva Explanation

Observer Pattern creates a one-to-many relationship.  
In FixMate, when the request status changes, multiple observers can react without tightly coupling notification logic with request logic.

---

# 6. Singleton Pattern

## Location

```text
com.fixmate.patterns.singleton
```

## File

```text
AppLogger.java
```

## Purpose

Singleton Pattern ensures only one object of a class exists in the whole application.

In FixMate, it is used for logging.

## Code Logic

```java
public class AppLogger {
    private static AppLogger instance;

    private AppLogger() {
    }

    public static synchronized AppLogger getInstance() {
        if (instance == null) {
            instance = new AppLogger();
        }
        return instance;
    }

    public void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }

    public void error(String message) {
        System.out.println("[ERROR] " + message);
    }
}
```

## Used In

```text
AssignmentService.java
ServiceRequestService.java
```

Example:

```java
private final AppLogger logger = AppLogger.getInstance();
```

## How It Works

Only one logger object is created:

```text
First call → creates AppLogger
Next calls → return same AppLogger object
```

## Viva Explanation

Singleton Pattern ensures one shared instance.  
In FixMate, `AppLogger` is a singleton, so all services use the same logger instance.

---

# 7. Adapter Pattern

## Location

```text
com.fixmate.patterns.adapter
```

## Files

```text
NotificationSender.java
EmailService.java
SmsService.java
EmailNotificationAdapter.java
SmsNotificationAdapter.java
```

## Purpose

Adapter Pattern allows incompatible classes to work through a common interface.

In FixMate:

```text
EmailService uses sendEmail()
SmsService uses sendSms()
```

But the application wants to use one common method:

```text
send()
```

Adapter Pattern solves this.

## Code Logic

### Target Interface

```java
public interface NotificationSender {
    void send(String recipient, String message);
}
```

### Email Service

```java
public class EmailService {
    public void sendEmail(String toAddress, String subject, String body) {
        System.out.println("Email sent to " + toAddress);
    }
}
```

### SMS Service

```java
public class SmsService {
    public void sendSms(String phoneNumber, String textMessage) {
        System.out.println("SMS sent to " + phoneNumber);
    }
}
```

### Email Adapter

```java
public class EmailNotificationAdapter implements NotificationSender {
    private final EmailService emailService;

    public EmailNotificationAdapter(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void send(String recipient, String message) {
        emailService.sendEmail(recipient, "FixMate Notification", message);
    }
}
```

### SMS Adapter

```java
public class SmsNotificationAdapter implements NotificationSender {
    private final SmsService smsService;

    public SmsNotificationAdapter(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void send(String recipient, String message) {
        smsService.sendSms(recipient, message);
    }
}
```

## Used In

```text
AssignmentService.java
```

Example:

```java
NotificationSender sender =
        new EmailNotificationAdapter(new EmailService());

sender.send(workerEmail, "You have been assigned a job.");
```

## How It Works

The system only calls:

```java
sender.send(...)
```

The adapter internally decides whether to call:

```text
sendEmail()
sendSms()
```

## Viva Explanation

Adapter Pattern converts one interface into another expected interface.  
In FixMate, different notification systems have different methods, but adapters make them usable through one common `NotificationSender` interface.

---

# Main Services Logic

## AuthService

### Purpose
Handles login.

### Logic

```text
1. Receive email and password
2. Find user by email
3. Compare stored password with entered password
4. If matched, return user data
5. If not matched, return invalid credentials
```

### Endpoint

```text
POST /api/auth/login
```

---

## UserService

### Purpose
Handles users.

### Logic

```text
1. Create users
2. Get all users
3. Get user by id
4. Delete user
```

### Used For

```text
Client registration
Worker creation by admin
Admin viewing workers
```

---

## ServiceRequestService

### Purpose
Handles booking requests.

### Logic

```text
1. Client creates request
2. Request is saved with PENDING status
3. Status can be updated to ASSIGNED, IN_PROGRESS, COMPLETED, or CANCELLED
```

### Important Status Flow

```text
PENDING
    ↓
ASSIGNED
    ↓
IN_PROGRESS
    ↓
COMPLETED
```

---

## AssignmentService

### Purpose
Handles assigning workers and assignment data.

### Logic

```text
1. Admin selects a pending request
2. Admin selects a worker
3. Assignment is created
4. Request status changes to ASSIGNED
5. Worker can see assigned job
6. Status updates are reflected to admin and client
```

### Patterns Used In AssignmentService

```text
Command
Observer
Adapter
Mediator
Singleton
Null Object
```

This is the best file to show during viva because many patterns are connected here.

---

# Important API Endpoints

## Authentication

```text
POST /api/auth/login
```

## Users

```text
POST /api/users
GET /api/users
GET /api/users/{id}
DELETE /api/users/{id}
```

## Services

```text
POST /api/services
GET /api/services
GET /api/services/{id}
DELETE /api/services/{id}
```

## Requests

```text
POST /api/requests
GET /api/requests
GET /api/requests/{id}
GET /api/requests/client/{clientId}
PUT /api/requests/{id}/status
```

## Assignments

```text
POST /api/assignments
GET /api/assignments
GET /api/assignments/worker/{workerId}
```

---

# Frontend Pages

## Client Pages

```text
/client/dashboard
/client/book-service
/client/requests
```

Client flow:
```text
Book service → track request status
```

## Admin Pages

```text
/admin/dashboard
/admin/requests
/admin/workers
```

Admin flow:
```text
View requests → assign worker → track status
```

## Worker Pages

```text
/worker/dashboard
/worker/jobs
```

Worker flow:
```text
View assigned jobs → start work → complete work
```

---

# Full Status Flow

## Step 1: Client Creates Request

Endpoint:
```text
POST /api/requests
```

Payload:
```json
{
  "clientId": 3,
  "serviceId": 1,
  "address": "Karachi Pakistan",
  "bookingDate": "2026-06-10",
  "bookingTime": "10:00:00",
  "issueDescription": "Kitchen pipe leakage"
}
```

Result:
```text
status = PENDING
```

---

## Step 2: Admin Assigns Worker

Endpoint:
```text
POST /api/assignments
```

Payload:
```json
{
  "serviceRequestId": 1,
  "workerId": 2
}
```

Result:
```text
status = ASSIGNED
```

---

## Step 3: Worker Starts Work

Endpoint:
```text
PUT /api/requests/{id}/status
```

Payload:
```json
{
  "status": "IN_PROGRESS"
}
```

Result:
```text
status = IN_PROGRESS
```

---

## Step 4: Worker Completes Work

Endpoint:
```text
PUT /api/requests/{id}/status
```

Payload:
```json
{
  "status": "COMPLETED"
}
```

Result:
```text
status = COMPLETED
```

---

# Best Files to Show Teacher

## For Design Patterns

```text
fixmate-backend/src/main/java/com/fixmate/patterns
```

## For Multiple Pattern Usage

```text
fixmate-backend/src/main/java/com/fixmate/service/AssignmentService.java
```

## For Request Status Logic

```text
fixmate-backend/src/main/java/com/fixmate/service/ServiceRequestService.java
```

## For Login Logic

```text
fixmate-backend/src/main/java/com/fixmate/service/AuthService.java
```

## For Database Models

```text
fixmate-backend/src/main/java/com/fixmate/model
```

---

# Viva Short Explanation

FixMate is a role-based home service booking system.

A client creates a booking request.  
The request is first saved as `PENDING`.  
Admin views the request and assigns a worker.  
After assignment, the request becomes `ASSIGNED`.  
The worker can start the job, changing the status to `IN_PROGRESS`.  
After completing the job, the worker changes the status to `COMPLETED`.

The project uses 7 design patterns:

```text
Factory     → creates service objects
Command     → wraps actions like assign worker and complete request
Null Object → avoids null worker errors
Mediator    → manages communication between client, admin, worker
Observer    → notifies on status changes
Singleton   → one shared logger instance
Adapter     → connects email/SMS notification services with common interface
```

---

# Notes

This project is built mainly for SDA demonstration.  
The design patterns are implemented in the Java backend and connected with the home service workflow.
