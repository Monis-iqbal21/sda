package com.fixmate.patterns.mediator;

// Mediator Pattern - Concrete Mediator
// Central hub: Client, Admin, and Worker never talk to each other directly.
// All communication is routed through FixMateMediator.
public class FixMateMediator implements ServiceMediator {

    @Override
    public void sendMessage(String from, String to, String message) {
        System.out.println("[FixMateMediator] Message from " + from
                + " to " + to + ": " + message);
    }

    @Override
    public void notifyStatusChange(Long requestId, String status) {
        // Mediator decides who to inform based on the new status.
        switch (status.toUpperCase()) {
            case "ASSIGNED" -> {
                sendMessage("SYSTEM", "CLIENT", "Request #" + requestId + " has been assigned to a worker.");
                sendMessage("SYSTEM", "WORKER", "You have been assigned to request #" + requestId + ".");
            }
            case "IN_PROGRESS" -> {
                sendMessage("SYSTEM", "CLIENT", "Request #" + requestId + " is now in progress.");
            }
            case "COMPLETED" -> {
                sendMessage("SYSTEM", "CLIENT", "Request #" + requestId + " has been completed.");
                sendMessage("SYSTEM", "ADMIN",  "Request #" + requestId + " was completed successfully.");
            }
            default -> sendMessage("SYSTEM", "ADMIN", "Request #" + requestId + " status: " + status);
        }
    }
}
