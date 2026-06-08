package com.fixmate.patterns.nullobject;

// Null Object Pattern - Real Implementation
// Represents an actual worker who has been assigned.
public class RealWorker implements Worker {

    private final String name;
    private final String email;

    public RealWorker(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getEmail() { return email; }

    @Override
    public boolean isNull() { return false; }

    @Override
    public void performWork(String taskDescription) {
        System.out.println("[RealWorker] " + name + " is working on: " + taskDescription);
    }
}
