package com.example.studentmanagement.exception;

public class CourseHasEnrollmentException extends RuntimeException {
    public CourseHasEnrollmentException(Long courseId) {
        super("Course " + courseId + " cannot be deleted because students are enrolled in it");
    }
}
