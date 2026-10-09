package com.example.studentmanagement.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

@Entity
@Table(name ="courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true , length = 20, nullable = false)
    private String code;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(nullable = false)
    private int credits;

    protected Course(){
        //
    }

    public Course(String code, int credits, String title) {
        this.code = code;
        this.credits = credits;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public int getCredits() {
        return credits;
    }
}
