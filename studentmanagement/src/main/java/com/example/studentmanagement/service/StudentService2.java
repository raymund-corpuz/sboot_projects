package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentRequest2;
import com.example.studentmanagement.dto.StudentResponse2;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.DuplicateEmailException;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentService2 {

    private final StudentRepository studentRepository;

    public StudentService2(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public List<StudentResponse2> getAllStudents(){
        return studentRepository.findAll().stream().map(StudentResponse2::from).toList();
    }

    public StudentResponse2 getStudentById(Long id){
        Student student = findStudentOrThrow(id);
        return StudentResponse2.from(student);
    }

  @Transactional
  public StudentResponse2 createStudent(StudentRequest2 request){

        if(studentRepository.existsByEmail(request.email())){
            throw new DuplicateEmailException(request.email());
        }

        Student student = new Student(request.firstName(), request.lastName(), request.email(), request.dateOfBirth());

        Student saved = studentRepository.save(student);

        return StudentResponse2.from(student);
  }

    @Transactional
    public StudentResponse2 updateStudent(Long id, StudentRequest2 request){
        Student student = findStudentOrThrow(id);

        boolean emailChanged = !student.getEmail().equals(request.email());

        if(emailChanged && studentRepository.existsByEmail(request.email())){
            throw new DuplicateEmailException(request.email());
        }

        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setDateOfBirth(request.dateOfBirth());

        return  StudentResponse2.from(student);

    }

    public void deleteStudent(Long id){
        Student student = findStudentOrThrow(id);
        studentRepository.delete(student);
    }

    private Student findStudentOrThrow(Long id){
       return studentRepository.findById(String.valueOf(id)).orElseThrow(()-> new StudentNotFoundException(id));
    }
}
