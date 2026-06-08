package com.fixmate.patterns.factory;

public class NotificationFactory {

    public static String createMessage(String type, String detail) {
        return switch (type.toUpperCase()) {
            case "ASSIGNED" -> "Your request has been assigned to a worker. Details: " + detail;
            case "COMPLETED" -> "Your service request has been completed. Details: " + detail;
            case "CANCELLED" -> "Your service request was cancelled. Details: " + detail;
            default -> "Update on your request: " + detail;
        };
    }
}
