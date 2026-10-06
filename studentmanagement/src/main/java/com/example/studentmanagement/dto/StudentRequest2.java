package com.example.studentmanagement.dto;

import java.time.LocalDate;

public record StudentRequest2(String firstName, String lastName, String email, LocalDate dateOfBirth) {
}
