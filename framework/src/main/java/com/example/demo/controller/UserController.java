package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.minidiframework.annotation.Autowired;
import com.example.minidiframework.annotation.Component;

import java.util.List;
import java.util.Optional;

/**
 * Controller for handling User-related requests.
 */
@Component
public class UserController {
    
    @Autowired
    private UserService userService;

    public List<User> getAllUsers() {
        System.out.println("UserController: Getting all users");
        return userService.getAllUsers();
    }

    public Optional<User> getUserById(Long id) {
        System.out.println("UserController: Getting user by id: " + id);
        return userService.getUserById(id);
    }

    public User createUser(String name, String email) {
        System.out.println("UserController: Creating user - " + name + ", " + email);
        return userService.createUser(name, email);
    }

    public User updateUser(Long id, String name, String email) {
        System.out.println("UserController: Updating user " + id + " - " + name + ", " + email);
        return userService.updateUser(id, name, email);
    }

    public void deleteUser(Long id) {
        System.out.println("UserController: Deleting user " + id);
        userService.deleteUser(id);
    }

    public List<User> searchUsers(String name) {
        System.out.println("UserController: Searching users by name: " + name);
        return userService.searchUsersByName(name);
    }

    public void printUserStatistics() {
        long count = userService.getUserCount();
        System.out.println("UserController: Total users in system: " + count);
    }
}
