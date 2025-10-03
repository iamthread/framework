package com.example.minidiframework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Manages configuration properties loaded from properties files.
 */
public class ConfigurationProperties {
    private final Properties properties = new Properties();
    
    public ConfigurationProperties() {
        loadDefaultProperties();
    }
    
    public ConfigurationProperties(String propertiesFile) {
        loadProperties(propertiesFile);
    }
    
    /**
     * Load default application.properties if it exists.
     */
    private void loadDefaultProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            // Default properties file is optional
        }
    }
    
    /**
     * Load properties from a specific file.
     */
    private void loadProperties(String propertiesFile) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + propertiesFile, e);
        }
    }
    
    /**
     * Get a property value.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get a property value with a default value.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get a property as an integer.
     */
    public Integer getIntProperty(String key) {
        String value = getProperty(key);
        return value != null ? Integer.parseInt(value) : null;
    }
    
    /**
     * Get a property as a boolean.
     */
    public Boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : null;
    }
    
    /**
     * Check if a property exists.
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
    
    /**
     * Get all property keys.
     */
    public java.util.Set<String> getPropertyKeys() {
        return properties.stringPropertyNames();
    }
}
