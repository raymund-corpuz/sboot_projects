package com.example.studentmanagement.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.http.HttpHeaders;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SecurityResponseWriter writer;

    public RestAuthenticationEntryPoint(SecurityResponseWriter writer){
        this.writer = writer;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
        writer.write(response, HttpStatus.UNAUTHORIZED,
                "Authentication is required: send a valid Bearer token");
    }
}
