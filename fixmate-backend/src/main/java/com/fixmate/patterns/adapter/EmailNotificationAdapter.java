package com.fixmate.patterns.adapter;

// Adapter Pattern - Concrete Adapter (Email)
// Wraps EmailService and makes it compatible with NotificationSender.
// The application calls send() — the adapter translates it to sendEmail().
public class EmailNotificationAdapter implements NotificationSender {

    private final EmailService emailService;

    public EmailNotificationAdapter(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void send(String recipient, String message) {
        // Translate the generic send() call into EmailService's specific method.
        emailService.sendEmail(recipient, "FixMate Notification", message);
    }
}
