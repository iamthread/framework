package com.example.minidiframework;

import com.example.minidiframework.annotation.*;
import com.example.minidiframework.context.ApplicationContext;
import com.example.minidiframework.injection.DependencyInjector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DependencyInjector.
 */
public class DependencyInjectorTest {

    @Test
    public void testFieldInjection() {
        ApplicationContext context = new ApplicationContext("com.example.minidiframework.test");
        
        TestServiceWithFieldInjection service = context.getBean(TestServiceWithFieldInjection.class);
        assertNotNull(service);
        assertNotNull(service.getRepository());
    }

    @Test
    public void testConstructorInjection() {
        ApplicationContext context = new ApplicationContext("com.example.minidiframework.test");
        
        TestServiceWithConstructorInjection service = context.getBean(TestServiceWithConstructorInjection.class);
        assertNotNull(service);
        assertNotNull(service.getRepository());
    }

    // Test classes
    @Service
    public static class TestServiceWithFieldInjection {
        @Autowired
        private TestRepository repository;

        public TestRepository getRepository() {
            return repository;
        }
    }

    @Service
    public static class TestServiceWithConstructorInjection {
        private final TestRepository repository;

        @Autowired
        public TestServiceWithConstructorInjection(TestRepository repository) {
            this.repository = repository;
        }

        public TestRepository getRepository() {
            return repository;
        }
    }

    @Repository
    public static class TestRepository {
        public String getData() {
            return "test data";
        }
    }
}
