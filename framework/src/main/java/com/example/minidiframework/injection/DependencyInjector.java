package com.example.minidiframework.injection;

import com.example.minidiframework.annotation.Autowired;
import com.example.minidiframework.annotation.Value;
import com.example.minidiframework.config.ConfigurationProperties;
import com.example.minidiframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Handles dependency injection for @Autowired fields and constructors.
 */
public class DependencyInjector {
    private final ApplicationContext applicationContext;
    private final ConfigurationProperties configurationProperties;

    public DependencyInjector(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.configurationProperties = new ConfigurationProperties();
    }

    /**
     * Perform dependency injection on all beans.
     */
    public void injectDependencies() {
        for (Object bean : applicationContext.getBeans().values()) {
            injectFields(bean);
        }
    }

    /**
     * Inject dependencies into @Autowired and @Value fields.
     */
    private void injectFields(Object bean) {
        Class<?> clazz = bean.getClass();
        
        // Inject into all fields in the class hierarchy
        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    injectField(bean, field);
                } else if (field.isAnnotationPresent(Value.class)) {
                    injectValue(bean, field);
                }
            }
            
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * Inject a specific field.
     */
    private void injectField(Object bean, Field field) {
        try {
            field.setAccessible(true);
            
            // Check if field is already set
            if (field.get(bean) != null) {
                return; // Already injected
            }
            
            // Find the appropriate bean to inject
            Object dependency = findDependency(field.getType());
            if (dependency != null) {
                field.set(bean, dependency);
            } else {
                throw new RuntimeException("No suitable bean found for field: " + 
                    field.getName() + " of type: " + field.getType().getName());
            }
            
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject field: " + field.getName(), e);
        }
    }

    /**
     * Inject a property value into a field.
     */
    private void injectValue(Object bean, Field field) {
        try {
            field.setAccessible(true);
            
            Value valueAnnotation = field.getAnnotation(Value.class);
            String propertyKey = valueAnnotation.value();
            String propertyValue = configurationProperties.getProperty(propertyKey);
            
            if (propertyValue == null) {
                throw new RuntimeException("Property not found: " + propertyKey);
            }
            
            // Convert the string value to the field type
            Object convertedValue = convertValue(propertyValue, field.getType());
            field.set(bean, convertedValue);
            
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject value for field: " + field.getName(), e);
        }
    }
    
    /**
     * Convert a string value to the target type.
     */
    private Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(value);
        } else if (targetType == Long.class || targetType == long.class) {
            return Long.parseLong(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (targetType == Double.class || targetType == double.class) {
            return Double.parseDouble(value);
        } else if (targetType == Float.class || targetType == float.class) {
            return Float.parseFloat(value);
        } else {
            throw new RuntimeException("Unsupported type for value injection: " + targetType.getName());
        }
    }

    /**
     * Find a dependency bean by type.
     */
    private Object findDependency(Class<?> type) {
        try {
            return applicationContext.getBean(type);
        } catch (RuntimeException e) {
            // Try to find by interface
            for (Object bean : applicationContext.getBeans().values()) {
                if (type.isAssignableFrom(bean.getClass())) {
                    return bean;
                }
            }
            return null;
        }
    }

    /**
     * Create a bean instance with constructor injection.
     */
    public Object createBeanWithConstructorInjection(Class<?> clazz) {
        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            
            // Find @Autowired constructor or constructor with parameters
            Constructor<?> targetConstructor = null;
            
            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(Autowired.class)) {
                    targetConstructor = constructor;
                    break;
                }
            }
            
            // If no @Autowired constructor, use the one with most parameters
            if (targetConstructor == null) {
                targetConstructor = Arrays.stream(constructors)
                    .max((c1, c2) -> Integer.compare(c1.getParameterCount(), c2.getParameterCount()))
                    .orElse(constructors[0]);
            }
            
            // Get constructor parameters
            Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = findDependency(parameterTypes[i]);
                if (parameters[i] == null) {
                    throw new RuntimeException("Cannot resolve dependency for constructor parameter: " + 
                        parameterTypes[i].getName());
                }
            }
            
            targetConstructor.setAccessible(true);
            return targetConstructor.newInstance(parameters);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean with constructor injection: " + clazz.getName(), e);
        }
    }
}
