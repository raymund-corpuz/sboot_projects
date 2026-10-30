package com.example.studentmanagement.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class SecurityResponseWriter {

    //Ony pass constant messages here: the text is not JSON-escaped
    public void write(HttpServletResponse response, HttpStatus status, String message)throws IOException{

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String body = """
                {"timestamp":"%s","status":%d,"error":"%s","message":"%s","fieldErrors":null}
                """.formatted(LocalDateTime.now(), status.value(), status.getReasonPhrase(),message);

        response.getWriter().write(body);
    }
}
