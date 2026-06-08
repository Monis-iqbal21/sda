package com.fixmate.patterns.nullobject;

// Null Object Pattern - Null Implementation
// Returned when no worker has been assigned yet.
// Prevents NullPointerException — callers treat it just like a RealWorker.
public class NullWorker implements Worker {

    @Override
    public String getName() { return "Unassigned"; }

    @Override
    public String getEmail() { return "N/A"; }

    @Override
    public boolean isNull() { return true; }

    @Override
    public void performWork(String taskDescription) {
        System.out.println("[NullWorker] No worker assigned. Task pending: " + taskDescription);
    }
}
