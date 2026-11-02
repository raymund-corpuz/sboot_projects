package com.example.studentmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;

    @Column(name = "family_id", nullable = false, length = 36)
    private String familyId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime revokedAt;

    protected RefreshToken(){
        //JPA
    }

    public RefreshToken(Long userId, String tokenHash, String familyId, LocalDateTime createdAt,
                        LocalDateTime expiresAt){
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.familyId =familyId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFamilyId(){
        return familyId;
    }

    public  boolean isRevoked(){
        return revokedAt !=null;
    }

    public boolean isExpired(LocalDateTime now){
        return !expiresAt.isAfter(now);
    }

    public void revoke(LocalDateTime now){
        this.revokedAt = now;
    }
}
