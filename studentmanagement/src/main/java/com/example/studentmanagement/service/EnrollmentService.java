package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.CourseRequest;
import com.example.studentmanagement.dto.CourseResponse;
import com.example.studentmanagement.dto.EnrollmentResponse;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.Enrollment;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.AlreadyEnrolledException;
import com.example.studentmanagement.exception.CourseNotFoundException;
import com.example.studentmanagement.exception.EnrollmentNotFoundException;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.repository.CourseRepository;
import com.example.studentmanagement.repository.EnrollmentRepository;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EnrollmentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(StudentRepository studentRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

  @Transactional
    public EnrollmentResponse enroll(Long studentId, Long courseId){
      Student student = studentRepository.findById(String.valueOf(studentId)).orElseThrow(() -> new StudentNotFoundException(studentId));
      Course course = courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundException(courseId));

      if(enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)){
          throw new AlreadyEnrolledException(studentId, courseId);
      }

      Enrollment enrollment = new Enrollment(student, course);

      Enrollment saved = enrollmentRepository.save(enrollment);

      return EnrollmentResponse.from(saved);
  }

  @Transactional
    public void unenroll(Long studentId, Long courseId){
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId,courseId).orElseThrow(()-> new AlreadyEnrolledException(studentId, courseId));

        enrollmentRepository.delete(enrollment);
  }

  public List<StudentResponse> getStudentsInCourse(Long courseId){
        if(!courseRepository.existsById(courseId)){
            throw new CourseNotFoundException(courseId);
        }
        return enrollmentRepository.findStudentByCourseId(courseId).stream().map(StudentResponse::from).toList();
  }

  public List<CourseResponse> getCoursesForStudent(Long studentId){
        if(!studentRepository.existsById(String.valueOf(studentId))){
            throw  new StudentNotFoundException(studentId);
        }
        return enrollmentRepository.findCourseByStudentId(studentId).stream().map(CourseResponse::from).toList();
  }
}
