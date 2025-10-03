# Mini Dependency Injection Framework

A lightweight dependency injection framework for Java, inspired by Spring Framework. This framework provides automatic component scanning, dependency injection, and bean management.

## Features

- **Component Scanning**: Automatically discovers classes annotated with `@Component`, `@Service`, `@Repository`, and `@Configuration`
- **Dependency Injection**: Supports both field and constructor injection using `@Autowired`
- **Configuration Properties**: Load and inject properties from `application.properties` using `@Value`
- **AOP Support**: Basic Aspect-Oriented Programming with `@Aspect`, `@Before`, and `@After` annotations
- **Web Layer**: Simple REST API support with `@RestController` and `@RequestMapping`
- **Bean Management**: Centralized bean lifecycle management
- **Type-safe**: Full type safety with generics support

## Quick Start

### 1. Add Dependencies

The framework uses Maven. Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>org.reflections</groupId>
    <artifactId>reflections</artifactId>
    <version>0.10.2</version>
</dependency>
```

### 2. Create Components

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

@Repository
public class UserRepository {
    public List<User> findAll() {
        // Implementation
    }
}
```

### 3. Initialize Application Context

```java
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext("com.example.myapp");
        
        UserService userService = context.getBean(UserService.class);
        List<User> users = userService.getAllUsers();
    }
}
```

## Annotations

### @Component
Marks a class as a component that should be managed by the framework.

```java
@Component
public class MyComponent {
    // Implementation
}
```

### @Service
Specialization of `@Component` for service layer components.

```java
@Service
public class UserService {
    // Business logic
}
```

### @Repository
Specialization of `@Component` for data access layer components.

```java
@Repository
public class UserRepository {
    // Data access logic
}
```

### @Autowired
Marks a field or constructor for automatic dependency injection.

```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    // Constructor injection
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### @Configuration
Marks a class as a configuration class.

```java
@Configuration
public class AppConfig {
    // Configuration methods
}
```

### @Value
Injects property values from configuration files.

```java
@Component
public class MyService {
    @Value("app.name")
    private String appName;
    
    @Value("server.port")
    private int serverPort;
}
```

### @Aspect, @Before, @After
Provides AOP (Aspect-Oriented Programming) capabilities.

```java
@Aspect
public class LoggingAspect {
    @Before("com.example.service.*.*")
    public void logBefore(Object target, Method method, Object[] args) {
        System.out.println("Before: " + method.getName());
    }
    
    @After("com.example.service.*.*")
    public void logAfter(Object target, Method method, Object[] args) {
        System.out.println("After: " + method.getName());
    }
}
```

### @RestController, @RequestMapping
Creates REST API endpoints.

```java
@RestController("/api/users")
public class UserController {
    @RequestMapping(value = "", method = "GET")
    public HttpResponse getAllUsers(HttpRequest request) {
        // Implementation
    }
    
    @RequestMapping(value = "/{id}", method = "GET")
    public HttpResponse getUserById(HttpRequest request) {
        // Implementation
    }
}
```

## API Reference

### ApplicationContext

The main entry point for the framework.

#### Constructor
```java
ApplicationContext(String basePackage)
```
Creates a new application context and scans the specified package for components.

#### Methods

- `Object getBean(String name)` - Get a bean by name
- `<T> T getBean(Class<T> type)` - Get a bean by type
- `<T> List<T> getBeansOfType(Class<T> type)` - Get all beans of a specific type
- `boolean containsBean(String name)` - Check if a bean exists
- `Set<String> getBeanNames()` - Get all bean names

## Example Application

See the `com.example.demo` package for a complete example application that demonstrates:

- User management with repository and service layers
- Automatic dependency injection
- Component scanning
- Bean retrieval and usage

## Building and Running

### Build the Framework
```bash
mvn clean compile
```

### Run the Demo Applications
```bash
# Basic demo
mvn exec:java -Dexec.mainClass="com.example.demo.DemoApplication"

# Enhanced demo with all features
mvn exec:java -Dexec.mainClass="com.example.demo.EnhancedDemoApplication"
```

### Run Tests
```bash
mvn test
```

## Architecture

The framework consists of several key components:

1. **ApplicationContext**: Central bean registry and management
2. **ComponentScanner**: Discovers annotated classes in the classpath
3. **DependencyInjector**: Handles field and constructor injection
4. **Annotations**: Mark classes and fields for framework processing

## Limitations

This is a mini framework designed for educational purposes. It has some limitations compared to full-featured frameworks:

- No AOP (Aspect-Oriented Programming) support
- No transaction management
- No configuration properties support
- Limited error handling and validation
- No support for circular dependencies

## Contributing

This framework is designed for learning purposes. Feel free to extend it with additional features like:

- Configuration properties support
- AOP capabilities
- Transaction management
- Better error handling
- Circular dependency detection
