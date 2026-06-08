package com.fixmate.patterns.factory;

// Factory Pattern - Product Interface
// Every concrete service type implements this contract.
public interface HomeService {
    String getServiceName();
    String getDescription();
    void execute();
}
