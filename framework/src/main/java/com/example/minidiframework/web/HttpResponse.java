package com.example.minidiframework.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP response.
 */
public class HttpResponse {
    private int statusCode;
    private String body;
    private Map<String, String> headers;

    public HttpResponse() {
        this.statusCode = 200;
        this.body = "";
        this.headers = new HashMap<>();
    }

    public HttpResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = new HashMap<>();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public static HttpResponse ok(String body) {
        return new HttpResponse(200, body);
    }

    public static HttpResponse notFound() {
        return new HttpResponse(404, "Not Found");
    }

    public static HttpResponse serverError(String message) {
        return new HttpResponse(500, message);
    }
}
