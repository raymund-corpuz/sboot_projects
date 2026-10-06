package com.example.studentmanagement.dto;

import com.example.studentmanagement.entity.Student;

import java.time.LocalDate;

public record StudentResponse2 (Long id, String firstName, String lastName, String email, LocalDate dateOfBirth){

    public static StudentResponse2 from(Student student){
        return new StudentResponse2(student.getId(), student.getFirstName(), student.getLastName(), student.getEmail(), student.getDateOfBirth());
    }
}
