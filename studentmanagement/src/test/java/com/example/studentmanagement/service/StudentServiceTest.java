package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.DuplicateEmailException;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.repository.EnrollmentRepository;
import com.example.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private StudentService studentService;

    private StudentRequest validRequest() {
        return new StudentRequest("Ana", "Santos", "ana@mail.com", LocalDate.of(2003, 5, 14));
    }

    @Test
    void createStudent_newEmail_savesAndReturnsResponse() {
        when(studentRepository.existsByEmail("ana@mail.com")).thenReturn(false);
        when(studentRepository.save(any(Student.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StudentResponse response = studentService.createStudent(validRequest());

        assertThat(response.email()).isEqualTo("ana@mail.com");
        assertThat(response.firstName()).isEqualTo("Ana");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void createStudent_duplicateEmail_throwsAndDoesNotSave() {
        when(studentRepository.existsByEmail("ana@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> studentService.createStudent(validRequest()))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("ana@mail.com");

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void getStudentById_missing_throwsNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Student not found with id: 99");
    }

    @Test
    void updateStudent_sameEmail_skipsDuplicateCheck() {
        Student existing = new Student("Ana", "Santos", "ana@mail.com", LocalDate.of(2003, 5, 14));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));

        StudentRequest request =
                new StudentRequest("Ana", "Reyes", "ana@mail.com", LocalDate.of(2003, 5, 14));
        StudentResponse response = studentService.updateStudent(1L, request);

        assertThat(response.lastName()).isEqualTo("Reyes");
        verify(studentRepository, never()).existsByEmail(anyString());
    }

    @Test
    void updateStudent_emailTakenByAnotherStudent_throws() {
        Student existing = new Student("Ana", "Santos", "ana@mail.com", LocalDate.of(2003, 5, 14));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.existsByEmail("ben@mail.com")).thenReturn(true);

        StudentRequest request =
                new StudentRequest("Ana", "Santos", "ben@mail.com", LocalDate.of(2003, 5, 14));

        assertThatThrownBy(() -> studentService.updateStudent(1L, request))
                .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void deleteStudent_removesEnrollmentsBeforeStudent() {
        Student existing = new Student("Ana", "Santos", "ana@mail.com", LocalDate.of(2003, 5, 14));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));

        studentService.deleteStudent(1L);

        InOrder order = inOrder(enrollmentRepository, studentRepository);
        order.verify(enrollmentRepository).deleteByStudentId(1L);
        order.verify(studentRepository).delete(existing);
    }
}