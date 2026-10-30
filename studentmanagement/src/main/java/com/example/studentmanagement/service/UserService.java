package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.CreateUserRequest;
import com.example.studentmanagement.dto.UserResponse;
import com.example.studentmanagement.entity.AppUser;
import com.example.studentmanagement.exception.DuplicateUsernameException;
import com.example.studentmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //No  @Transactional : BCrypt is slow
    public UserResponse createUser(CreateUserRequest request){
        String username = request.username().trim().toLowerCase(Locale.ROOT);

        if(userRepository.existsByUsername(username)){
            throw new DuplicateUsernameException(username);
        }

        AppUser user = new AppUser(username, request.password(), request.role());

        return UserResponse.from(userRepository.save(user));
    }

}
