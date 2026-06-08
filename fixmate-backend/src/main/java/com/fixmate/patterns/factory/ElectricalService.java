package com.fixmate.patterns.factory;

// Factory Pattern - Concrete Product
public class ElectricalService implements HomeService {

    @Override
    public String getServiceName() {
        return "Electrical";
    }

    @Override
    public String getDescription() {
        return "Wiring, fuse, and electrical fault repair";
    }

    @Override
    public void execute() {
        System.out.println("[ElectricalService] Executing electrical service.");
    }
}
