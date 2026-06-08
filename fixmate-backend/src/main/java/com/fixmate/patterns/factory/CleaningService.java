package com.fixmate.patterns.factory;

// Factory Pattern - Concrete Product
public class CleaningService implements HomeService {

    @Override
    public String getServiceName() {
        return "Cleaning";
    }

    @Override
    public String getDescription() {
        return "Home and office deep cleaning";
    }

    @Override
    public void execute() {
        System.out.println("[CleaningService] Executing cleaning service.");
    }
}
