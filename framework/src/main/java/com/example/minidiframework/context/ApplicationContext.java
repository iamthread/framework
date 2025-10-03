package com.example.minidiframework.context;

import com.example.minidiframework.annotation.*;
import com.example.minidiframework.scanner.ComponentScanner;
import com.example.minidiframework.injection.DependencyInjector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The main application context that manages beans and handles dependency injection.
 * This is the central component of the mini framework.
 */
public class ApplicationContext {
    private final Map<String, Object> beans = new ConcurrentHashMap<>();
    private final Map<Class<?>, String> typeToBeanName = new ConcurrentHashMap<>();
    private final ComponentScanner componentScanner;
    private final DependencyInjector dependencyInjector;

    public ApplicationContext(String basePackage) {
        this.componentScanner = new ComponentScanner(basePackage);
        this.dependencyInjector = new DependencyInjector(this);
        initializeContext();
    }

    /**
     * Initialize the application context by scanning for components and creating beans.
     */
    private void initializeContext() {
        // Scan for components
        Set<Class<?>> componentClasses = componentScanner.scanForComponents();
        
        // Create beans for all discovered components
        for (Class<?> componentClass : componentClasses) {
            createBean(componentClass);
        }
        
        // Perform dependency injection
        dependencyInjector.injectDependencies();
    }

    /**
     * Create a bean instance for the given component class.
     */
    private void createBean(Class<?> componentClass) {
        try {
            String beanName = getBeanName(componentClass);
            
            // Find the appropriate constructor (prefer @Autowired constructor)
            Constructor<?> constructor = findAutowiredConstructor(componentClass);
            if (constructor == null) {
                constructor = componentClass.getDeclaredConstructor();
            }
            
            constructor.setAccessible(true);
            Object instance = constructor.newInstance();
            
            // Register the bean
            beans.put(beanName, instance);
            typeToBeanName.put(componentClass, beanName);
            
            // Also register by interface if applicable
            registerByInterfaces(componentClass, beanName, instance);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean for class: " + componentClass.getName(), e);
        }
    }

    /**
     * Find a constructor annotated with @Autowired, or return null if none found.
     */
    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return constructor;
            }
        }
        
        // If no @Autowired constructor, look for constructor with parameters
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() > 0) {
                return constructor;
            }
        }
        
        return null;
    }

    /**
     * Register bean by its interfaces.
     */
    private void registerByInterfaces(Class<?> componentClass, String beanName, Object instance) {
        Class<?>[] interfaces = componentClass.getInterfaces();
        for (Class<?> interfaceClass : interfaces) {
            typeToBeanName.put(interfaceClass, beanName);
        }
    }

    /**
     * Get the bean name for a component class.
     */
    private String getBeanName(Class<?> componentClass) {
        // Check for explicit name in annotations
        if (componentClass.isAnnotationPresent(Component.class)) {
            Component component = componentClass.getAnnotation(Component.class);
            if (!component.value().isEmpty()) {
                return component.value();
            }
        }
        
        if (componentClass.isAnnotationPresent(Service.class)) {
            Service service = componentClass.getAnnotation(Service.class);
            if (!service.value().isEmpty()) {
                return service.value();
            }
        }
        
        if (componentClass.isAnnotationPresent(Repository.class)) {
            Repository repository = componentClass.getAnnotation(Repository.class);
            if (!repository.value().isEmpty()) {
                return repository.value();
            }
        }
        
        // Default to class name with lowercase first letter
        String className = componentClass.getSimpleName();
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }

    /**
     * Get a bean by name.
     */
    public Object getBean(String name) {
        return beans.get(name);
    }

    /**
     * Get a bean by type.
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> type) {
        String beanName = typeToBeanName.get(type);
        if (beanName == null) {
            throw new RuntimeException("No bean found for type: " + type.getName());
        }
        return (T) beans.get(beanName);
    }

    /**
     * Get all beans of a specific type.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getBeansOfType(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Object bean : beans.values()) {
            if (type.isAssignableFrom(bean.getClass())) {
                result.add((T) bean);
            }
        }
        return result;
    }

    /**
     * Check if a bean exists.
     */
    public boolean containsBean(String name) {
        return beans.containsKey(name);
    }

    /**
     * Get all bean names.
     */
    public Set<String> getBeanNames() {
        return beans.keySet();
    }

    /**
     * Get the internal beans map (for testing purposes).
     */
    Map<String, Object> getBeans() {
        return beans;
    }
}
