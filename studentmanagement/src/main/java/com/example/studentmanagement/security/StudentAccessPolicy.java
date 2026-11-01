package com.example.studentmanagement.security;


import com.example.studentmanagement.entity.AppUser;
import com.example.studentmanagement.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;




@Component("studentAccess")
public class StudentAccessPolicy {

    private final UserRepository userRepository;

    public StudentAccessPolicy(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public boolean canView(Authentication authentication, Long studentId){

        if(hasRole(authentication, "STUDENT")){
            return userRepository.findByUsername(authentication.getName())
                    .map(AppUser::getStudentId)
                    .map(ownId -> ownId.equals(studentId))
                    .orElse(false);
        }
        return false;
    }

    private boolean hasRole(Authentication authentication, String role){
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}
