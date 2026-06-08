package com.fixmate.patterns.observer;

// Observer Pattern - Observer Interface
// Any class that wants to react to request status changes implements this.
public interface StatusObserver {
    void onStatusChanged(Long requestId, String newStatus, String targetEmail);
}
