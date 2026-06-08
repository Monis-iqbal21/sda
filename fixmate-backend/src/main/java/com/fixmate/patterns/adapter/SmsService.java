package com.fixmate.patterns.adapter;

// Adapter Pattern - Adaptee (third-party / legacy SMS service)
// Has its own method signature that is incompatible with NotificationSender.
public class SmsService {

    public void sendSms(String phoneNumber, String textMessage) {
        System.out.println("[SmsService] Sending SMS to: " + phoneNumber);
        System.out.println("  Message : " + textMessage);
    }
}
