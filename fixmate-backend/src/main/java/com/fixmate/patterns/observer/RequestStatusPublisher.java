package com.fixmate.patterns.observer;

import java.util.ArrayList;
import java.util.List;

// Observer Pattern - Subject (Publisher)
// Maintains a list of observers and notifies them on status changes.
public class RequestStatusPublisher {

    private final List<StatusObserver> observers = new ArrayList<>();

    public void subscribe(StatusObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(StatusObserver observer) {
        observers.remove(observer);
    }

    // Called whenever a ServiceRequest status changes.
    public void notifyObservers(Long requestId, String newStatus, String targetEmail) {
        for (StatusObserver observer : observers) {
            observer.onStatusChanged(requestId, newStatus, targetEmail);
        }
    }
}
