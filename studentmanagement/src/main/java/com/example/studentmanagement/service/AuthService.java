package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.RegisterRequest;
import com.example.studentmanagement.dto.UserResponse;
import com.example.studentmanagement.entity.AppUser;
import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.exception.DuplicateUsernameException;
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

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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

    public UserResponse login(LoginRequest request){
        String username = normalize(request.username());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, request.password())
        );

        AppUser user = userRepository.findByUsername(username).orElseThrow(()-> new BadCredentialsException("Invalid credentials"));

        return UserResponse.from(user);
    }

    private String normalize(String username){
        return username.trim().toLowerCase(Locale.ROOT);
    }
}
