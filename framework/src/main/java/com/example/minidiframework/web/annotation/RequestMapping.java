package com.example.minidiframework.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps a method to a URL path and HTTP method.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    /**
     * The URL path to map to.
     */
    String value() default "";
    
    /**
     * The HTTP method (GET, POST, PUT, DELETE).
     */
    String method() default "GET";
}
