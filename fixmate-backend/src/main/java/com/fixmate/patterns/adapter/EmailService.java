package com.fixmate.patterns.adapter;

// Adapter Pattern - Adaptee (third-party / legacy email service)
// Has its own method signature that is incompatible with NotificationSender.
public class EmailService {

    public void sendEmail(String toAddress, String subject, String body) {
        System.out.println("[EmailService] Sending email to: " + toAddress);
        System.out.println("  Subject : " + subject);
        System.out.println("  Body    : " + body);
    }
}
