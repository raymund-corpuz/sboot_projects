package com.example.studentmanagement.exception;

public class StudentAlreadyLinkedException extends RuntimeException {
    public StudentAlreadyLinkedException(Long studentId) {

        super("Student " + studentId +" is already linked to a user account");
    }
}
