package com.example.demo;

import com.example.demo.config.AppConfig;
import com.example.demo.controller.UserController;
import com.example.demo.web.UserRestController;
import com.example.minidiframework.context.ApplicationContext;
import com.example.minidiframework.web.HttpRequest;
import com.example.minidiframework.web.HttpResponse;
import com.example.minidiframework.web.WebServer;

/**
 * Enhanced demo application showcasing all framework capabilities.
 */
public class EnhancedDemoApplication {
    public static void main(String[] args) {
        System.out.println("🚀 === Enhanced Mini Framework Demo ===");
        System.out.println();
        
        // Initialize the application context
        System.out.println("📦 Initializing ApplicationContext...");
        ApplicationContext context = new ApplicationContext("com.example.demo");
        System.out.println("✅ ApplicationContext initialized successfully!");
        System.out.println();
        
        // Demonstrate configuration properties
        demonstrateConfiguration(context);
        
        // Demonstrate dependency injection and AOP
        demonstrateDependencyInjectionAndAOP(context);
        
        // Demonstrate web layer
        demonstrateWebLayer(context);
        
        System.out.println("🎉 === Demo completed successfully! ===");
    }
    
    private static void demonstrateConfiguration(ApplicationContext context) {
        System.out.println("⚙️ === Configuration Properties Demo ===");
        AppConfig appConfig = context.getBean(AppConfig.class);
        appConfig.printConfiguration();
        System.out.println();
    }
    
    private static void demonstrateDependencyInjectionAndAOP(ApplicationContext context) {
        System.out.println("🔧 === Dependency Injection & AOP Demo ===");
        System.out.println("Note: AOP logging will be shown during method calls");
        System.out.println();
        
        UserController userController = context.getBean(UserController.class);
        
        // These calls will trigger AOP logging
        System.out.println("📋 Getting all users (with AOP logging):");
        userController.getAllUsers().forEach(System.out::println);
        System.out.println();
        
        System.out.println("🔍 Searching for users named 'John' (with AOP logging):");
        userController.searchUsers("John").forEach(System.out::println);
        System.out.println();
        
        System.out.println("📊 User statistics (with AOP logging):");
        userController.printUserStatistics();
        System.out.println();
    }
    
    private static void demonstrateWebLayer(ApplicationContext context) {
        System.out.println("🌐 === Web Layer Demo ===");
        
        // Create web server
        WebServer webServer = new WebServer(context);
        webServer.start(8080);
        System.out.println();
        
        // Simulate HTTP requests
        System.out.println("📡 Simulating HTTP requests:");
        
        // GET /api/users
        HttpRequest getUsersRequest = new HttpRequest("GET", "/api/users");
        HttpResponse response = webServer.handleRequest(getUsersRequest);
        System.out.println("GET /api/users -> " + response.getStatusCode() + ": " + response.getBody());
        
        // GET /api/users/1
        HttpRequest getUserRequest = new HttpRequest("GET", "/api/users/1");
        getUserRequest.getParameters().put("id", "1");
        response = webServer.handleRequest(getUserRequest);
        System.out.println("GET /api/users/1 -> " + response.getStatusCode() + ": " + response.getBody());
        
        // POST /api/users
        HttpRequest createUserRequest = new HttpRequest("POST", "/api/users");
        createUserRequest.getParameters().put("name", "Alice Wonder");
        createUserRequest.getParameters().put("email", "alice@wonderland.com");
        response = webServer.handleRequest(createUserRequest);
        System.out.println("POST /api/users -> " + response.getStatusCode() + ": " + response.getBody());
        
        // GET /api/users/search
        HttpRequest searchRequest = new HttpRequest("GET", "/api/users/search");
        searchRequest.getParameters().put("name", "Alice");
        response = webServer.handleRequest(searchRequest);
        System.out.println("GET /api/users/search?name=Alice -> " + response.getStatusCode() + ": " + response.getBody());
        
        // DELETE /api/users/1
        HttpRequest deleteRequest = new HttpRequest("DELETE", "/api/users/1");
        deleteRequest.getParameters().put("id", "1");
        response = webServer.handleRequest(deleteRequest);
        System.out.println("DELETE /api/users/1 -> " + response.getStatusCode() + ": " + response.getBody());
        
        System.out.println();
    }
}
