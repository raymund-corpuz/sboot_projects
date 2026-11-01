package com.example.studentmanagement.dto;

import com.example.studentmanagement.entity.AppUser;
import com.example.studentmanagement.entity.Role;


public record UserResponse(Long id, String username, Role role, Long studentId) {

    public static UserResponse from(AppUser user){
        return new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.getStudentId());

    }
}
