package com.fixmate.patterns.nullobject;

// Null Object Pattern - Abstract Target
// Both RealWorker and NullWorker implement this so callers never need null checks.
public interface Worker {
    String getName();
    String getEmail();
    boolean isNull();
    void performWork(String taskDescription);
}
