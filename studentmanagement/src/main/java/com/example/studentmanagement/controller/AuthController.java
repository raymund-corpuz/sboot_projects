package com.example.studentmanagement.controller;


import com.example.studentmanagement.dto.*;
import com.example.studentmanagement.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request){
        return  authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletRequest httpServletRequest){
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(authService.login(request, httpServletRequest.getRemoteAddr()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request){

        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(authService.refresh(request.refreshToken()));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody RefreshRequest request){
        authService.logout(request.refreshToken());
    }
}
