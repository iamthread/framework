package com.example.minidiframework.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request.
 */
public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final Map<String, String> parameters;
    private final String body;

    public HttpRequest(String method, String path) {
        this.method = method;
        this.path = path;
        this.headers = new HashMap<>();
        this.parameters = new HashMap<>();
        this.body = "";
    }

    public HttpRequest(String method, String path, Map<String, String> headers, 
                      Map<String, String> parameters, String body) {
        this.method = method;
        this.path = path;
        this.headers = headers != null ? headers : new HashMap<>();
        this.parameters = parameters != null ? parameters : new HashMap<>();
        this.body = body != null ? body : "";
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }
}
