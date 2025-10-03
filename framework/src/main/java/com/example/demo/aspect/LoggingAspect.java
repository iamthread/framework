package com.example.demo.aspect;

import com.example.minidiframework.annotation.After;
import com.example.minidiframework.annotation.Aspect;
import com.example.minidiframework.annotation.Before;

import java.lang.reflect.Method;

/**
 * Logging aspect for demonstration of AOP functionality.
 */
@Aspect(order = 1)
public class LoggingAspect {

    @Before("com.example.demo.service.*.*")
    public void logBeforeService(Object target, Method method, Object[] args) {
        System.out.println("🔍 [AOP] Before calling service method: " + 
            method.getDeclaringClass().getSimpleName() + "." + method.getName());
    }

    @After("com.example.demo.service.*.*")
    public void logAfterService(Object target, Method method, Object[] args) {
        System.out.println("✅ [AOP] After calling service method: " + 
            method.getDeclaringClass().getSimpleName() + "." + method.getName());
    }

    @Before("com.example.demo.repository.*.*")
    public void logBeforeRepository(Object target, Method method, Object[] args) {
        System.out.println("📊 [AOP] Before calling repository method: " + 
            method.getDeclaringClass().getSimpleName() + "." + method.getName());
    }

    @After("com.example.demo.repository.*.*")
    public void logAfterRepository(Object target, Method method, Object[] args) {
        System.out.println("💾 [AOP] After calling repository method: " + 
            method.getDeclaringClass().getSimpleName() + "." + method.getName());
    }
}
