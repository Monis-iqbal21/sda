package com.fixmate.patterns.observer;

public class NotificationObserver implements EventObserver {

    @Override
    public void onEvent(String eventType, Object data) {
        System.out.println("[NotificationObserver] Event: " + eventType + " | Data: " + data);
    }
}
