package com.fixmate.patterns.factory;

// Factory Pattern - Creator
// Admin calls ServiceFactory.create("Plumbing") instead of using 'new' directly.
// This decouples object creation from business logic.
public class ServiceFactory {

    public static HomeService create(String serviceType) {
        return switch (serviceType.toUpperCase()) {
            case "PLUMBING"   -> new PlumbingService();
            case "ELECTRICAL" -> new ElectricalService();
            case "CLEANING"   -> new CleaningService();
            default -> throw new IllegalArgumentException("Unknown service type: " + serviceType);
        };
    }
}
