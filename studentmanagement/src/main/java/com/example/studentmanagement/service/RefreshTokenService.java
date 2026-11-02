package com.example.studentmanagement.service;

import com.example.studentmanagement.config.JwtProperties;
import com.example.studentmanagement.entity.RefreshToken;
import com.example.studentmanagement.exception.InvalidRefreshTokenException;
import com.example.studentmanagement.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

    public record Rotation(Long userId, String newRawToken){
        // code...
    }
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService (RefreshTokenRepository refreshTokenRepository, JwtProperties jwtProperties){
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProperties = jwtProperties;
    }

    //called at login: starts a new family
    @Transactional
    public String issueNewFamily(Long userId){
        return  create(userId, UUID.randomUUID().toString());
    }

    //noRollbackfor: the family revocation on reuse Must be committed even
    //though we throw. Without it the exception would roll the revocation back

    @Transactional(noRollbackFor = InvalidRefreshTokenException.class)
    public Rotation rotate(String rawToken){

        RefreshToken current = refreshTokenRepository.findByTokenHashForUpdate(hash(rawToken)).orElseThrow(InvalidRefreshTokenException::new);
        LocalDateTime now =  nowUtc();

        if(current.isRevoked()){
            refreshTokenRepository.revokeFamily(current.getFamilyId(), now);
            log.warn("Refresh token reuse detected for user id {}; token family revoked", current.getUserId());
            throw  new InvalidRefreshTokenException();
        }

        if(current.isExpired(now)){
            throw new InvalidRefreshTokenException();
        }

        current.revoke(now);
        String next = create(current.getUserId(), current.getFamilyId());
        return new Rotation(current.getUserId(), next);
    }

    private String create(Long userId, String familyId){
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        LocalDateTime now = nowUtc();
        refreshTokenRepository.save(new RefreshToken(userId, hash(raw), familyId, now, now.plus(jwtProperties.refreshTokenTtl())));
        return raw;
    }

    private static LocalDateTime nowUtc(){
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    private static String hash(String raw){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(raw.getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException ex){
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
