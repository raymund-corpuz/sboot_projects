package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.RegisterRequest;
import com.example.studentmanagement.dto.TokenResponse;
import com.example.studentmanagement.dto.UserResponse;
import com.example.studentmanagement.entity.AppUser;
import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.exception.DuplicateUsernameException;
import com.example.studentmanagement.exception.InvalidRefreshTokenException;
import com.example.studentmanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,JwtService jwtService,
                       RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public UserResponse register(RegisterRequest request){
        String username = normalize(request.username());

        if(userRepository.existsByUsername(username)){
            throw  new DuplicateUsernameException(username);
        }

        //The role is Always Student here; it never comes from the client
        AppUser user = new AppUser(username, passwordEncoder.encode(request.password()), Role.STUDENT);

        return UserResponse.from(userRepository.save(user));
    }

    public TokenResponse login(LoginRequest request){
        String username = normalize(request.username());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, request.password()));

        AppUser user = userRepository.findByUsername(username).orElseThrow(()-> new BadCredentialsException("Invalid credentials"));

        //JwtService.AccessToken token = jwtService.createAccessToken(user);
        return issueTokens(user, refreshTokenService.issueNewFamily(user.getId()));

    }

    public TokenResponse refresh(String rawRefreshToken){
        RefreshTokenService.Rotation rotation = refreshTokenService.rotate(rawRefreshToken);

        //A disabled or deleted user cannot refresh
        AppUser user = userRepository.findById(rotation.userId())
                .filter(AppUser::isEnabled)
                .orElseThrow(InvalidRefreshTokenException::new);

        return issueTokens(user, rotation.newRawToken());
    }

    public void logout(String rawRefreshToken){
        refreshTokenService.revoke(rawRefreshToken);
    }

    private TokenResponse issueTokens(AppUser user, String refreshToken){
        JwtService.AccessToken accessToken = jwtService.createAccessToken(user);

        return new TokenResponse(accessToken.value(), refreshToken, "Bearer",
                accessToken.expiresInSeconds(), UserResponse.from(user));
    }

    private String normalize(String username){

        return username.trim().toLowerCase(Locale.ROOT);
    }
}
