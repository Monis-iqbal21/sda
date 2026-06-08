package com.fixmate.patterns.mediator;

// Mediator Pattern - Mediator Interface
// Defines how participants (Client, Admin, Worker) communicate indirectly.
public interface ServiceMediator {
    void sendMessage(String from, String to, String message);
    void notifyStatusChange(Long requestId, String status);
}
