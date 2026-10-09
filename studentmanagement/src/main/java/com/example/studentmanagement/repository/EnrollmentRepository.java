package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Course;
import com.example.studentmanagement.entity.Enrollment;
import com.example.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByCourseId(Long courseId);

    Optional<Enrollment> findStudentIdAndCourseId(Long studentId, Long courseId);

    void deleteByStudentId(Long studentId);

    @Query("select e.student from Enrollment e where e.course.id = :courseId")
    List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);

    @Query("select e.course from Enrollment e where e.studen.id = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);
}
