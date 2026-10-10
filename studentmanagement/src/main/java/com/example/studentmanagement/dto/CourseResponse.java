package com.example.studentmanagement.dto;

import com.example.studentmanagement.entity.Course;

public record CourseResponse(Long id, String code, String title, int credits) {

    public static CourseResponse from(Course course){
        return new CourseResponse(course.getId(), course.getCode(), course.getTitle(), course.getCredits());
    }
}
