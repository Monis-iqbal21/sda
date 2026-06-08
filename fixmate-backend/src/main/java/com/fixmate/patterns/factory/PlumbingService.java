package com.fixmate.patterns.factory;

// Factory Pattern - Concrete Product
public class PlumbingService implements HomeService {

    @Override
    public String getServiceName() {
        return "Plumbing";
    }

    @Override
    public String getDescription() {
        return "Pipe and water leakage repair";
    }

    @Override
    public void execute() {
        System.out.println("[PlumbingService] Executing plumbing service.");
    }
}
