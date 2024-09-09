package com.example.field_service_management.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomWebAuthenticationDetailsSource {

    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        // Implement your custom details logic here
        // For example, you could return a new WebAuthenticationDetails instance
        return new WebAuthenticationDetails((javax.servlet.http.HttpServletRequest) request);
    }
}
