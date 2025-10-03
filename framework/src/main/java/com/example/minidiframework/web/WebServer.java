package com.example.minidiframework.web;

import com.example.minidiframework.annotation.RequestMapping;
import com.example.minidiframework.annotation.RestController;
import com.example.minidiframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple web server that handles HTTP requests and routes them to controllers.
 */
public class WebServer {
    private final ApplicationContext applicationContext;
    private final Map<String, Method> routeHandlers = new HashMap<>();
    private final Map<String, Object> controllerInstances = new HashMap<>();

    public WebServer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initializeRoutes();
    }

    /**
     * Initialize routes by scanning for @RestController and @RequestMapping annotations.
     */
    private void initializeRoutes() {
        for (Object bean : applicationContext.getBeans().values()) {
            if (bean.getClass().isAnnotationPresent(RestController.class)) {
                processController(bean);
            }
        }
    }

    /**
     * Process a controller and register its routes.
     */
    private void processController(Object controller) {
        RestController restController = controller.getClass().getAnnotation(RestController.class);
        String basePath = restController.value();

        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                String path = basePath + mapping.value();
                String httpMethod = mapping.method().toUpperCase();
                String routeKey = httpMethod + ":" + path;

                routeHandlers.put(routeKey, method);
                controllerInstances.put(routeKey, controller);
            }
        }
    }

    /**
     * Handle an HTTP request.
     */
    public HttpResponse handleRequest(HttpRequest request) {
        try {
            String routeKey = request.getMethod().toUpperCase() + ":" + request.getPath();
            Method handler = routeHandlers.get(routeKey);

            if (handler == null) {
                return HttpResponse.notFound();
            }

            Object controller = controllerInstances.get(routeKey);
            handler.setAccessible(true);
            Object result = handler.invoke(controller, request);

            if (result instanceof HttpResponse) {
                return (HttpResponse) result;
            } else {
                return HttpResponse.ok(result.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.serverError("Internal Server Error: " + e.getMessage());
        }
    }

    /**
     * Start the web server (simplified version).
     */
    public void start(int port) {
        System.out.println("Web server started on port " + port);
        System.out.println("Available routes:");
        for (String route : routeHandlers.keySet()) {
            System.out.println("  " + route);
        }
    }
}
