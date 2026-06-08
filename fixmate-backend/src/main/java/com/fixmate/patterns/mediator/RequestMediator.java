package com.fixmate.patterns.mediator;

public interface RequestMediator {
    void notify(Object sender, String event);
}
