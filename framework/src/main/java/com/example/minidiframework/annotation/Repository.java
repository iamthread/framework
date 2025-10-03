package com.example.minidiframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specialization of @Component for data access layer components.
 * Repositories typically handle data persistence.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Repository {
    /**
     * The name of the repository. If not specified, the class name with lowercase first letter is used.
     */
    String value() default "";
}
