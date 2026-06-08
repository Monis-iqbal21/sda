# Adapter Pattern Diagram

## Explanation
NotificationSender is the target interface the system uses. EmailService and SmsService are incompatible third-party classes with different method signatures. EmailNotificationAdapter and SmsNotificationAdapter wrap them to implement NotificationSender, letting the system send notifications through any channel without changing the calling code.

## Mermaid

```mermaid
classDiagram
    class NotificationSender {
        <<interface>>
        +send(to: String, message: String) void
    }

    class EmailService {
        +sendEmail(to: String, body: String) void
    }

    class SmsService {
        +sendSms(phone: String, text: String) void
    }

    class EmailNotificationAdapter {
        -EmailService emailService
        +send(to: String, message: String) void
    }

    class SmsNotificationAdapter {
        -SmsService smsService
        +send(to: String, message: String) void
    }

    NotificationSender <|.. EmailNotificationAdapter
    NotificationSender <|.. SmsNotificationAdapter
    EmailNotificationAdapter --> EmailService : wraps
    SmsNotificationAdapter --> SmsService : wraps
```
