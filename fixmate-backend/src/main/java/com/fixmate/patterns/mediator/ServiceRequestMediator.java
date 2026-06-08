package com.fixmate.patterns.mediator;

public class ServiceRequestMediator implements RequestMediator {

    @Override
    public void notify(Object sender, String event) {
        System.out.println("[Mediator] Received event: " + event + " from: " + sender.getClass().getSimpleName());
    }
}
