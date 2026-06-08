package com.fixmate.patterns.command;

import java.util.ArrayList;
import java.util.List;

// Command Pattern - Invoker
// Holds and executes commands without knowing their implementation.
// Supports a command history for potential undo support in the future.
public class RequestInvoker {

    private final List<Command> history = new ArrayList<>();

    public void executeCommand(Command command) {
        command.execute();
        history.add(command);
    }

    public List<Command> getHistory() {
        return history;
    }
}
