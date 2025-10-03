package com.example.minidiframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a component that should be managed by the framework.
 * Components are automatically discovered and instantiated by the ApplicationContext.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * The name of the component. If not specified, the class name with lowercase first letter is used.
     */
    String value() default "";
}
