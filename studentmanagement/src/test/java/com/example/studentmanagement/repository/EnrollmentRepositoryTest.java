package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.Enrollment;
import com.example.studentmanagement.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// Boot 4 package. On Boot 3 use:
// org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EnrollmentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void findByEmail_existingEmail_returnsStudent() {
        studentRepository.save(
                new Student("Ana", "Santos", "ana@mail.com", LocalDate.of(2003, 5, 14)));

        assertThat(studentRepository.findByEmail("ana@mail.com")).isPresent();
        assertThat(studentRepository.existsByEmail("nobody@mail.com")).isFalse();
    }

    @Test
    void findStudentsAndCourses_returnOnlyMatchingRows() {
        Student ana = studentRepository.save(
                new Student("Ana", "Santos", "ana@mail.com", LocalDate.of(2003, 5, 14)));
        Student ben = studentRepository.save(
                new Student("Ben", "Cruz", "ben@mail.com", LocalDate.of(2002, 1, 20)));
        Course cs101 = courseRepository.save(new Course("CS101", "Intro to Programming", 3));
        Course ma201 = courseRepository.save(new Course("MA201", "Calculus", 4));

        enrollmentRepository.save(new Enrollment(ana, cs101));
        enrollmentRepository.save(new Enrollment(ben, cs101));
        enrollmentRepository.save(new Enrollment(ana, ma201));

        List<Student> inCs101 = enrollmentRepository.findStudentByCourseId(cs101.getId());
        List<Course> anasCourses = enrollmentRepository.findCoursesByStudentId(ana.getId());

        assertThat(inCs101).extracting(Student::getEmail)
                .containsExactlyInAnyOrder("ana@mail.com", "ben@mail.com");
        assertThat(anasCourses).extracting(Course::getCode)
                .containsExactlyInAnyOrder("CS101", "MA201");
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(ben.getId(), ma201.getId()))
                .isFalse();
        assertThat(enrollmentRepository.existsByCourseId(cs101.getId())).isTrue();
    }
}