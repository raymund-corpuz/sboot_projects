package com.example.studentmanagement.exception;

public class EnrollmentNotFoundException extends RuntimeException {
    public EnrollmentNotFoundException(Long studentId, Long courseId) {

        super("Student " + studentId + " is not enrolled in course " + courseId);
    }
}
