package com.example.minidiframework;

import com.example.minidiframework.annotation.*;
import com.example.minidiframework.context.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ApplicationContext.
 */
public class ApplicationContextTest {

    @Test
    public void testBeanCreationAndRetrieval() {
        ApplicationContext context = new ApplicationContext("com.example.minidiframework.test");
        
        // Test that beans are created
        assertTrue(context.containsBean("testService"));
        assertTrue(context.containsBean("testRepository"));
        
        // Test bean retrieval by name
        Object service = context.getBean("testService");
        assertNotNull(service);
        assertTrue(service instanceof TestService);
        
        // Test bean retrieval by type
        TestService serviceByType = context.getBean(TestService.class);
        assertNotNull(serviceByType);
        assertSame(service, serviceByType);
    }

    @Test
    public void testDependencyInjection() {
        ApplicationContext context = new ApplicationContext("com.example.minidiframework.test");
        
        TestService service = context.getBean(TestService.class);
        assertNotNull(service);
        
        // Verify that the repository was injected
        assertNotNull(service.getRepository());
        assertTrue(service.getRepository() instanceof TestRepository);
    }

    @Test
    public void testGetBeansOfType() {
        ApplicationContext context = new ApplicationContext("com.example.minidiframework.test");
        
        // Get all components
        var components = context.getBeansOfType(Object.class);
        assertTrue(components.size() >= 2); // At least service and repository
        
        // Get all services
        var services = context.getBeansOfType(TestService.class);
        assertEquals(1, services.size());
    }

    // Test classes for the framework
    @Service
    public static class TestService {
        @Autowired
        private TestRepository repository;

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
