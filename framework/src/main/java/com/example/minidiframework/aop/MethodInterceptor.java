package com.example.minidiframework.aop;

import java.lang.reflect.Method;

/**
 * Interface for method interceptors in AOP.
 */
public interface MethodInterceptor {
    /**
     * Invoke the interceptor.
     * @param target The target object
     * @param method The method being called
     * @param args The method arguments
     * @return The result of the method call
     * @throws Throwable If an error occurs
     */
    Object invoke(Object target, Method method, Object[] args) throws Throwable;
}
