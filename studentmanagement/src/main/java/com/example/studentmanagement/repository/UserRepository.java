package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<AppUser> findByStudentId(Long studentId);

    @Modifying
    @Query("update AppUser u set u.studentId = null where u.studentId = :studentId")
    int unlinkStudent(@Param("studentId") Long studentId);


}
