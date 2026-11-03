package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.CreateUserRequest;
import com.example.studentmanagement.dto.UserResponse;
import com.example.studentmanagement.entity.AppUser;
import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.exception.*;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       PasswordEncoder passwordEncoder,
                       RefreshTokenService refreshTokenService){
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
    }

    //No  @Transactional : BCrypt is slow
    public UserResponse createUser(CreateUserRequest request){
        String username = request.username().trim().toLowerCase(Locale.ROOT);

        if(userRepository.existsByUsername(username)){
            throw new DuplicateUsernameException(username);
        }

        AppUser user = new AppUser(username, passwordEncoder.encode(request.password()), request.role());

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse linkStudent(Long userId, Long studentId){
        AppUser user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));

        if(!studentRepository.existsById(studentId)){
            throw new StudentNotFoundException(studentId);
        }

        if(user.getRole() != Role.STUDENT){
            throw new InvalidUserOperationException("Only STUDENT accounts can be linked to a student record");
        }

        userRepository.findByStudentId(studentId)
                .filter(other -> !other.getId().equals(userId))
                .ifPresent(other -> {throw new StudentAlreadyLinkedException(studentId);
                });

        user.linkStudent(studentId);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse setEnabled(Long userId, boolean enabled, String actingUsername){

        AppUser user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException( userId));

        if(!enabled && user.getUsername().equals(actingUsername)){
            throw new InvalidUserOperationException("You cannot disable your own account");
        }

        user.setEnabled(enabled);
        if(!enabled){
            refreshTokenService.revokeAllForUser(userId); //ends their sessions
        }

        return UserResponse.from(user);
    }



}
