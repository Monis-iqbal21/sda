package com.fixmate.patterns.observer;

public interface EventObserver {
    void onEvent(String eventType, Object data);
}
