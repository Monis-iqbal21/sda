package com.fixmate.patterns.singleton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Singleton Pattern - Only ONE AppLogger instance exists in the entire application.
// Private constructor prevents external instantiation.
// Thread-safe via synchronized getInstance().
public class AppLogger {

    private static AppLogger instance;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Private constructor — nobody outside can call 'new AppLogger()'
    private AppLogger() {
        System.out.println("[AppLogger] Logger initialized.");
    }

    // Synchronized ensures only one instance even under concurrent access.
    public static synchronized AppLogger getInstance() {
        if (instance == null) {
            instance = new AppLogger();
        }
        return instance;
    }

    public void info(String message) {
        System.out.println("[INFO]  " + timestamp() + " - " + message);
    }

    public void warn(String message) {
        System.out.println("[WARN]  " + timestamp() + " - " + message);
    }

    public void error(String message) {
        System.out.println("[ERROR] " + timestamp() + " - " + message);
    }

    private String timestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
