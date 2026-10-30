package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.CreateUserRequest;
import com.example.studentmanagement.dto.UserResponse;
import com.example.studentmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request){
        return  userService.createUser(request);
    }
}
