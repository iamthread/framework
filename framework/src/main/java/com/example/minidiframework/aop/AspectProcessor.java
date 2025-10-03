package com.example.minidiframework.aop;

import com.example.minidiframework.annotation.After;
import com.example.minidiframework.annotation.Aspect;
import com.example.minidiframework.annotation.Before;
import com.example.minidiframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes aspects and creates method interceptors.
 */
public class AspectProcessor {
    private final ApplicationContext applicationContext;
    private final List<AspectInfo> aspects = new ArrayList<>();

    public AspectProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        processAspects();
    }

    /**
     * Process all aspect beans and create interceptors.
     */
    private void processAspects() {
        for (Object bean : applicationContext.getBeans().values()) {
            if (bean.getClass().isAnnotationPresent(Aspect.class)) {
                processAspect(bean);
            }
        }
    }

    /**
     * Process a single aspect bean.
     */
    private void processAspect(Object aspectBean) {
        Aspect aspectAnnotation = aspectBean.getClass().getAnnotation(Aspect.class);
        int order = aspectAnnotation.order();

        for (Method method : aspectBean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                Before beforeAnnotation = method.getAnnotation(Before.class);
                aspects.add(new AspectInfo(aspectBean, method, beforeAnnotation.value(), 
                    AspectType.BEFORE, order));
            } else if (method.isAnnotationPresent(After.class)) {
                After afterAnnotation = method.getAnnotation(After.class);
                aspects.add(new AspectInfo(aspectBean, method, afterAnnotation.value(), 
                    AspectType.AFTER, order));
            }
        }
    }

    /**
     * Get interceptors for a specific method.
     */
    public List<MethodInterceptor> getInterceptors(Method method) {
        List<MethodInterceptor> interceptors = new ArrayList<>();
        String methodSignature = method.getDeclaringClass().getName() + "." + method.getName();

        for (AspectInfo aspectInfo : aspects) {
            if (matchesPointcut(methodSignature, aspectInfo.pointcut)) {
                interceptors.add(new AspectMethodInterceptor(aspectInfo));
            }
        }

        // Sort by order
        interceptors.sort((a, b) -> {
            AspectMethodInterceptor ai = (AspectMethodInterceptor) a;
            AspectMethodInterceptor bi = (AspectMethodInterceptor) b;
            return Integer.compare(ai.aspectInfo.order, bi.aspectInfo.order);
        });

        return interceptors;
    }

    /**
     * Simple pointcut matching (supports wildcards).
     */
    private boolean matchesPointcut(String methodSignature, String pointcut) {
        // Simple wildcard matching
        if (pointcut.contains("*")) {
            String pattern = pointcut.replace("*", ".*");
            return methodSignature.matches(pattern);
        }
        return methodSignature.equals(pointcut);
    }

    /**
     * Aspect information holder.
     */
    private static class AspectInfo {
        final Object aspectBean;
        final Method aspectMethod;
        final String pointcut;
        final AspectType type;
        final int order;

        AspectInfo(Object aspectBean, Method aspectMethod, String pointcut, AspectType type, int order) {
            this.aspectBean = aspectBean;
            this.aspectMethod = aspectMethod;
            this.pointcut = pointcut;
            this.type = type;
            this.order = order;
        }
    }

    /**
     * Aspect type enumeration.
     */
    private enum AspectType {
        BEFORE, AFTER
    }

    /**
     * Method interceptor implementation for aspects.
     */
    private static class AspectMethodInterceptor implements MethodInterceptor {
        private final AspectInfo aspectInfo;

        AspectMethodInterceptor(AspectInfo aspectInfo) {
            this.aspectInfo = aspectInfo;
        }

        @Override
        public Object invoke(Object target, Method method, Object[] args) throws Throwable {
            aspectInfo.aspectMethod.setAccessible(true);
            return aspectInfo.aspectMethod.invoke(aspectInfo.aspectBean, target, method, args);
        }
    }
}
