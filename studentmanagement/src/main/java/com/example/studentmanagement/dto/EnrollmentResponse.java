package com.example.studentmanagement.dto;

import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.Enrollment;
import com.example.studentmanagement.entity.Student;

import java.time.LocalDateTime;

public record EnrollmentResponse(Long id, Long studentId, Long courseId, LocalDateTime enrolledAt) {

    public static EnrollmentResponse from(Enrollment enrollment){
        return new EnrollmentResponse(enrollment.getId(),enrollment.getStudent().getId(),enrollment.getCourse().getId(),enrollment.getEnrolledAt());
    }
}
