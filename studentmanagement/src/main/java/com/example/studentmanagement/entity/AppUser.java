package com.example.studentmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "users")
public class AppUser {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 50, unique = true)
        private String username;

        @Column(name = "password_hash", nullable = false, length = 100)
        private String passwordHash;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 20)
        private Role role;

        @Column(nullable = false)
        private boolean enabled;

        @Column(nullable = false)
        private LocalDateTime createdAt;

        protected AppUser(){
            //JPA
        }

        public AppUser(String username, String passwordHash, Role role) {
            this.username = username;
            this.passwordHash = passwordHash;
            this.role = role;
            this.enabled = true;
            this.createdAt = LocalDateTime.now();
        }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
