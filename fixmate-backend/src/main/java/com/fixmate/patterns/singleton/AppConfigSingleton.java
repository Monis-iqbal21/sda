package com.fixmate.patterns.singleton;

public class AppConfigSingleton {

    private static AppConfigSingleton instance;

    private String appName = "FixMate";
    private String version = "1.0.0";

    private AppConfigSingleton() {}

    public static synchronized AppConfigSingleton getInstance() {
        if (instance == null) {
            instance = new AppConfigSingleton();
        }
        return instance;
    }

    public String getAppName() { return appName; }
    public String getVersion() { return version; }
}
