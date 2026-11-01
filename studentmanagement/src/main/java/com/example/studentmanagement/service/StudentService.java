package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.DuplicateEmailException;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.repository.EnrollmentRepository;
import com.example.studentmanagement.repository.StudentRepository;

import com.example.studentmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.swing.text.html.parser.Entity;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository, UserRepository userRepository){
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public List<StudentResponse> getAllStudents(){
        return studentRepository.findAll().stream().map(StudentResponse::from).toList();
    }

    public StudentResponse getStudentById(Long id){
        Student student = findStudentOrThrow(id);
        return StudentResponse.from(student);
    }

    @Transactional
    public StudentResponse createStudent( StudentRequest request){
        if(studentRepository.existsByEmail(request.email())){
            throw new DuplicateEmailException(request.email());
        }

        Student student = new Student(request.firstName(),request.lastName(),request.email(),request.dateOfBirth());

        Student saved = studentRepository.save(student);
        return StudentResponse.from(saved);
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request){
        Student student = findStudentOrThrow(id);

        boolean emailChanged = !student.getEmail().equals(request.email());

        if(emailChanged && studentRepository.existsByEmail(request.email())){
            throw new DuplicateEmailException(request.email());
        }

        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setDateOfBirth(request.dateOfBirth());

        return StudentResponse.from(student);
    }

    @Transactional
    public void deleteStudent(Long id){
        Student student = findStudentOrThrow(id);
        enrollmentRepository.deleteByStudentId(id);

        userRepository.unlinkStudent(id);
        studentRepository.delete(student);
    }

    public Student findStudentOrThrow(Long id){
        return studentRepository.findById(id).orElseThrow(()-> new StudentNotFoundException(id));
    }
}
