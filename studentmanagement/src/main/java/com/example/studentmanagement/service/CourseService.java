package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.CourseRequest;
import com.example.studentmanagement.dto.CourseResponse;
import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.exception.CourseHasEnrollmentException;
import com.example.studentmanagement.exception.CourseNotFoundException;
import com.example.studentmanagement.exception.DuplicateCourseCodeException;
import com.example.studentmanagement.repository.CourseRepository;
import com.example.studentmanagement.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository){
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<CourseResponse> getAllCourses(){
        return courseRepository.findAll().stream().map(CourseResponse::from).toList();
    }

    public CourseResponse getCourseById(Long id){
        Course course = getIdOrThrow(id);
        return CourseResponse.from(course);
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest request){
        String code = request.code().trim().toUpperCase(Locale.ROOT);

        if(courseRepository.existByCode(request.code())){
            throw new DuplicateCourseCodeException(request.code());
        }

        Course course = new Course(code, request.title().trim(), request.credits());

        Course save = courseRepository.save(course);

        return CourseResponse.from(save);
    }

    @Transactional
    public void deleteCourse(Long id){
        Course course = getIdOrThrow(id);
        if(enrollmentRepository.existsByCourseId(id)){
            throw  new CourseHasEnrollmentException(id);
        }
        courseRepository.delete(course);
    }

    private Course getIdOrThrow(Long id){
        courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundException(id));
    }
}
