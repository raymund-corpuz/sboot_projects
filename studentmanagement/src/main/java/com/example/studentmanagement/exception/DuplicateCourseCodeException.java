package com.example.studentmanagement.exception;

public class DuplicateCourseCodeException extends RuntimeException {
    public DuplicateCourseCodeException(String code) {

        super("A Course code with '" + code +"' already exists");
    }
}
