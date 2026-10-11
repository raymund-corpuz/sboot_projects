package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.CourseResponse;
import com.example.studentmanagement.dto.EnrollmentResponse;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Enrollment;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.CourseRepository;
import com.example.studentmanagement.repository.EnrollmentRepository;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.CourseService;
import com.example.studentmanagement.service.EnrollmentService;
import com.example.studentmanagement.service.StudentService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<EnrollmentResponse> enroll(@PathVariable Long studentId, Long courseId){

        EnrollmentResponse enrollment = enrollmentService.enroll(studentId, courseId);

        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenroll(@PathVariable Long studentId, Long courseId){
        enrollmentService.unenroll(studentId,courseId);
    }

    @GetMapping("/students/{courseId}/students")
    public List<StudentResponse> getStudentsInCourse(@PathVariable Long courseId){
        return enrollmentService.getStudentsInCourse(courseId);
    }

    @GetMapping("/courses/{studentId}/courses")
    public List<CourseResponse> getAllCoursesForStudent(@PathVariable Long studentId){
        return enrollmentService.getCoursesForStudent(studentId);
    }
}
