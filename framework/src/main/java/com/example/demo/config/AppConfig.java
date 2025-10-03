package com.example.demo.config;

import com.example.minidiframework.annotation.Component;
import com.example.minidiframework.annotation.Value;

/**
 * Configuration class demonstrating @Value annotation usage.
 */
@Component
public class AppConfig {
    
    @Value("app.name")
    private String appName;
    
    @Value("app.version")
    private String appVersion;
    
    @Value("app.debug")
    private boolean debugMode;
    
    @Value("server.port")
    private int serverPort;
    
    @Value("feature.user.management")
    private boolean userManagementEnabled;
    
    @Value("db.host")
    private String databaseHost;
    
    @Value("db.port")
    private int databasePort;

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public int getServerPort() {
        return serverPort;
    }

    public boolean isUserManagementEnabled() {
        return userManagementEnabled;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }
    
    public void printConfiguration() {
        System.out.println("=== Application Configuration ===");
        System.out.println("App Name: " + appName);
        System.out.println("App Version: " + appVersion);
        System.out.println("Debug Mode: " + debugMode);
        System.out.println("Server Port: " + serverPort);
        System.out.println("User Management: " + userManagementEnabled);
        System.out.println("Database Host: " + databaseHost);
        System.out.println("Database Port: " + databasePort);
        System.out.println("==================================");
    }
}
