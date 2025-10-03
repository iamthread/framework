package com.example.demo.web;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.minidiframework.annotation.Autowired;
import com.example.minidiframework.web.annotation.RequestMapping;
import com.example.minidiframework.web.annotation.RestController;
import com.example.minidiframework.web.HttpRequest;
import com.example.minidiframework.web.HttpResponse;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for User operations.
 */
@RestController("/api/users")
public class UserRestController {
    
    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = "GET")
    public HttpResponse getAllUsers(HttpRequest request) {
        List<User> users = userService.getAllUsers();
        return HttpResponse.ok("Users: " + users.toString());
    }

    @RequestMapping(value = "/{id}", method = "GET")
    public HttpResponse getUserById(HttpRequest request) {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            return HttpResponse.serverError("ID parameter is required");
        }
        
        try {
            Long id = Long.parseLong(idParam);
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return HttpResponse.ok("User: " + user.get().toString());
            } else {
                return HttpResponse.notFound();
            }
        } catch (NumberFormatException e) {
            return HttpResponse.serverError("Invalid ID format");
        }
    }

    @RequestMapping(value = "", method = "POST")
    public HttpResponse createUser(HttpRequest request) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        
        if (name == null || email == null) {
            return HttpResponse.serverError("Name and email are required");
        }
        
        User user = userService.createUser(name, email);
        return HttpResponse.ok("Created user: " + user.toString());
    }

    @RequestMapping(value = "/{id}", method = "PUT")
    public HttpResponse updateUser(HttpRequest request) {
        String idParam = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        
        if (idParam == null || name == null || email == null) {
            return HttpResponse.serverError("ID, name and email are required");
        }
        
        try {
            Long id = Long.parseLong(idParam);
            User user = userService.updateUser(id, name, email);
            return HttpResponse.ok("Updated user: " + user.toString());
        } catch (NumberFormatException e) {
            return HttpResponse.serverError("Invalid ID format");
        } catch (RuntimeException e) {
            return HttpResponse.serverError(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = "DELETE")
    public HttpResponse deleteUser(HttpRequest request) {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            return HttpResponse.serverError("ID parameter is required");
        }
        
        try {
            Long id = Long.parseLong(idParam);
            userService.deleteUser(id);
            return HttpResponse.ok("User deleted successfully");
        } catch (NumberFormatException e) {
            return HttpResponse.serverError("Invalid ID format");
        }
    }

    @RequestMapping(value = "/search", method = "GET")
    public HttpResponse searchUsers(HttpRequest request) {
        String name = request.getParameter("name");
        if (name == null) {
            return HttpResponse.serverError("Name parameter is required for search");
        }
        
        List<User> users = userService.searchUsersByName(name);
        return HttpResponse.ok("Search results: " + users.toString());
    }
}
