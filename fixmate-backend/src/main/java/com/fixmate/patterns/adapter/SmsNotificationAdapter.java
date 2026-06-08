package com.fixmate.patterns.adapter;

// Adapter Pattern - Concrete Adapter (SMS)
// Wraps SmsService and makes it compatible with NotificationSender.
// The application calls send() — the adapter translates it to sendSms().
public class SmsNotificationAdapter implements NotificationSender {

    private final SmsService smsService;

    public SmsNotificationAdapter(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void send(String recipient, String message) {
        // Translate the generic send() call into SmsService's specific method.
        smsService.sendSms(recipient, message);
    }
}
