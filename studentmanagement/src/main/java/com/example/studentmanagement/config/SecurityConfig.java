package com.example.studentmanagement.config;

import com.example.studentmanagement.security.RestAccessDeniedHandler;
import com.example.studentmanagement.security.RestAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(RestAuthenticationEntryPoint authenticationEntryPoint,
                          RestAccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        // Spring's error dispatch; permitting it avoids masking real errors
                        .requestMatchers("/error").permitAll()

                        // public
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

                        // administration
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // courses
                        .requestMatchers(HttpMethod.GET, "/api/courses", "/api/courses/*")
                        .hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/courses").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/*").hasRole("ADMIN")

                        // enrollments
                        .requestMatchers(HttpMethod.GET, "/api/courses/*/students")
                        .hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/courses/*/students/*")
                        .hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/*/students/*")
                        .hasAnyRole("ADMIN", "TEACHER")

                        // students
                        .requestMatchers(HttpMethod.GET, "/api/students", "/api/students/*",
                                "/api/students/*/courses")
                        .hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/students/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/*").hasRole("ADMIN")

                        // deny by default: anything not listed above
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("roles");
        authorities.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}