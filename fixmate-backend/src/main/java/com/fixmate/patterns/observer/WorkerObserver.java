package com.fixmate.patterns.observer;

// Observer Pattern - Concrete Observer
// Notifies the assigned worker when a request status changes.
public class WorkerObserver implements StatusObserver {

    @Override
    public void onStatusChanged(Long requestId, String newStatus, String targetEmail) {
        System.out.println("[WorkerObserver] Dear Worker (" + targetEmail + "), "
                + "request #" + requestId + " status updated to: " + newStatus);
    }
}
