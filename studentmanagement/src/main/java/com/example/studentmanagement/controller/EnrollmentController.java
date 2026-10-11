package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.CourseResponse;
import com.example.studentmanagement.dto.EnrollmentResponse;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {

        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<EnrollmentResponse> enroll(@PathVariable Long courseId, @PathVariable Long studentId){

        EnrollmentResponse enrollment = enrollmentService.enroll(studentId, courseId);

        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    @DeleteMapping("/courses/{courseId}/students/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenroll(@PathVariable Long courseId, @PathVariable Long studentId){
        enrollmentService.unenroll(studentId,courseId);
    }

    @GetMapping("/courses/{courseId}/students")
    public List<StudentResponse> getStudentsInCourse(@PathVariable Long courseId){
        return enrollmentService.getStudentsInCourse(courseId);
    }

    @GetMapping("/students/{studentId}/courses")
    public List<CourseResponse> getCoursesForStudent(@PathVariable Long studentId){
        return enrollmentService.getCoursesForStudent(studentId);
    }
}
