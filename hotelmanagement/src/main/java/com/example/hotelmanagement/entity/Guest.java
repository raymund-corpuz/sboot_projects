package com.example.hotelmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "guests")
public class Guest {

    //id, firstName,lastName, email, phone, numberOfBookings
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 11, nullable = false)
    private String phone;

    @Column(length = 3, nullable = false)
    private int numberOfBookings;


    protected Guest(){
        //JPA
    }

    public Guest(String firstName, String lastName, String email, String phone, int numberOfBookings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.numberOfBookings = numberOfBookings;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getNumberOfBookings() {
        return numberOfBookings;
    }

}
