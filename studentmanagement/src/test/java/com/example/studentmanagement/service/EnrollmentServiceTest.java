package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.EnrollmentResponse;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private final Student ana =
            new Student("Ana", "Santos", "ana@mail.com", LocalDate.of(2003, 5, 14));
    private final Course cs101 = new Course("CS101", "Intro to Programming", 3);

    @Test
    void enroll_validStudentAndCourse_savesEnrollment() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(ana));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(cs101));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EnrollmentResponse response = enrollmentService.enroll(1L, 2L);

        assertThat(response.enrolledAt()).isNotNull();
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void enroll_alreadyEnrolled_throwsAndDoesNotSave() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(ana));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(cs101));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enroll(1L, 2L))
                .isInstanceOf(AlreadyEnrolledException.class);

        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void enroll_unknownStudent_throwsStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enroll(1L, 2L))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void enroll_unknownCourse_throwsCourseNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(ana));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.enroll(1L, 2L))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void unenroll_notEnrolled_throwsEnrollmentNotFound() {
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 2L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.unenroll(1L, 2L))
                .isInstanceOf(EnrollmentNotFoundException.class)
                .hasMessage("Student 1 is not enrolled in course 2");
    }

    @Test
    void unenroll_enrolled_deletesEnrollment() {
        Enrollment enrollment = new Enrollment(ana, cs101);
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 2L))
                .thenReturn(Optional.of(enrollment));

        enrollmentService.unenroll(1L, 2L);

        verify(enrollmentRepository).delete(enrollment);
    }
}