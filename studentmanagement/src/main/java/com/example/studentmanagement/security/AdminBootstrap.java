package com.example.studentmanagement.security;

import com.example.studentmanagement.entity.AppUser;
import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AdminBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);
    private static final int MIN_PASSWORD_LENGTH = 12;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public AdminBootstrap(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          @Value("${app.bootstrap.admin.username}") String adminUsername,
                          @Value("${app.bootstrap.admin.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        String username = adminUsername.trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(username)) {
            log.info("Bootstrap admin already exists; nothing to do");
            return;
        }

        if (adminPassword == null || adminPassword.length() < MIN_PASSWORD_LENGTH) {
            log.warn("No bootstrap admin created. Set BOOTSTRAP_ADMIN_PASSWORD "
                    + "(at least {} characters)", MIN_PASSWORD_LENGTH);
            return;
        }

        userRepository.save(
                new AppUser(username, passwordEncoder.encode(adminPassword), Role.ADMIN));
        log.info("Bootstrap admin '{}' created", username);
    }
}