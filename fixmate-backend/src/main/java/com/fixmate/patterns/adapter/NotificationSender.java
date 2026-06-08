package com.fixmate.patterns.adapter;

// Adapter Pattern - Target Interface
// The application always calls this interface — it never knows whether
// the underlying channel is Email, SMS, or anything else.
public interface NotificationSender {
    void send(String recipient, String message);
}
