package com.fixmate.patterns.observer;

// Observer Pattern - Concrete Observer
// Notifies the client when their request status changes.
public class ClientObserver implements StatusObserver {

    @Override
    public void onStatusChanged(Long requestId, String newStatus, String targetEmail) {
        System.out.println("[ClientObserver] Dear Client (" + targetEmail + "), "
                + "your request #" + requestId + " is now: " + newStatus);
    }
}
