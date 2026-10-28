package com.example.studentmanagement.dto;

import com.example.studentmanagement.entity.AppUser;
import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.repository.UserRepository;

public record UserResponse(Long id, String username, Role role) {

    public static UserResponse from(AppUser user){
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());

    }
}
