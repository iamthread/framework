package com.example.minidiframework.scanner;

import com.example.minidiframework.annotation.Component;
import com.example.minidiframework.annotation.Service;
import com.example.minidiframework.annotation.Repository;
import com.example.minidiframework.annotation.Configuration;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Scans the classpath for classes annotated with framework annotations.
 */
public class ComponentScanner {
    private final String basePackage;

    public ComponentScanner(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * Scan for all component classes in the base package.
     */
    public Set<Class<?>> scanForComponents() {
        Set<Class<?>> components = new HashSet<>();
        
        try {
            String packagePath = basePackage.replace('.', '/');
            URL resource = Thread.currentThread().getContextClassLoader().getResource(packagePath);
            
            if (resource == null) {
                throw new RuntimeException("Package not found: " + basePackage);
            }
            
            File packageDir = new File(resource.getFile());
            scanDirectory(packageDir, basePackage, components);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to scan for components", e);
        }
        
        return components;
    }

    /**
     * Recursively scan a directory for component classes.
     */
    private void scanDirectory(File directory, String packageName, Set<Class<?>> components) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }
        
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                // Recursively scan subdirectories
                String subPackage = packageName + "." + file.getName();
                scanDirectory(file, subPackage, components);
            } else if (file.getName().endsWith(".class")) {
                // Process class files
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                processClass(className, components);
            }
        }
    }

    /**
     * Process a class and add it to components if it's annotated.
     */
    private void processClass(String className, Set<Class<?>> components) {
        try {
            Class<?> clazz = Class.forName(className);
            
            // Check if the class is annotated with any of our component annotations
            if (clazz.isAnnotationPresent(Component.class) ||
                clazz.isAnnotationPresent(Service.class) ||
                clazz.isAnnotationPresent(Repository.class) ||
                clazz.isAnnotationPresent(Configuration.class)) {
                
                // Skip abstract classes and interfaces
                if (!clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                    components.add(clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            // Ignore classes that can't be loaded
        } catch (NoClassDefFoundError e) {
            // Ignore classes with missing dependencies
        }
    }
}
