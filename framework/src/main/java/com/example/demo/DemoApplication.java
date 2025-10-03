package com.example.demo;

import com.example.demo.controller.UserController;
import com.example.demo.config.AppConfig;
import com.example.demo.model.User;
import com.example.minidiframework.context.ApplicationContext;

import java.util.List;
import java.util.Optional;

/**
 * Demo application showcasing the mini framework capabilities.
 */
public class DemoApplication {
    public static void main(String[] args) {
        System.out.println("=== Mini Framework Demo ===");
        System.out.println();
        
        // Initialize the application context
        System.out.println("Initializing ApplicationContext...");
        ApplicationContext context = new ApplicationContext("com.example.demo");
        System.out.println("ApplicationContext initialized successfully!");
        System.out.println();
        
        // Get the beans
        UserController userController = context.getBean(UserController.class);
        AppConfig appConfig = context.getBean(AppConfig.class);
        
        // Show configuration
        appConfig.printConfiguration();
        System.out.println();
        
        // Demonstrate the framework functionality
        demonstrateFramework(userController);
    }
    
    private static void demonstrateFramework(UserController userController) {
        System.out.println("=== Framework Demonstration ===");
        System.out.println();
        
        // 1. Get all users
        System.out.println("1. Getting all users:");
        List<User> allUsers = userController.getAllUsers();
        allUsers.forEach(System.out::println);
        System.out.println();
        
        // 2. Get user by ID
        System.out.println("2. Getting user by ID (1):");
        Optional<User> user = userController.getUserById(1L);
        user.ifPresent(System.out::println);
        System.out.println();
        
        // 3. Create a new user
        System.out.println("3. Creating a new user:");
        User newUser = userController.createUser("Alice Brown", "alice@example.com");
        System.out.println("Created: " + newUser);
        System.out.println();
        
        // 4. Update a user
        System.out.println("4. Updating user (2):");
        User updatedUser = userController.updateUser(2L, "Jane Smith Updated", "jane.updated@example.com");
        System.out.println("Updated: " + updatedUser);
        System.out.println();
        
        // 5. Search users
        System.out.println("5. Searching users by name (John):");
        List<User> searchResults = userController.searchUsers("John");
        searchResults.forEach(System.out::println);
        System.out.println();
        
        // 6. Print statistics
        System.out.println("6. User statistics:");
        userController.printUserStatistics();
        System.out.println();
        
        // 7. Show all users after changes
        System.out.println("7. All users after changes:");
        allUsers = userController.getAllUsers();
        allUsers.forEach(System.out::println);
        System.out.println();
        
        System.out.println("=== Demo completed successfully! ===");
    }
}
