package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentRequest;
import com.example.studentmanagement.dto.StudentResponse;
import com.example.studentmanagement.exception.DuplicateEmailException;
import com.example.studentmanagement.exception.StudentNotFoundException;
import com.example.studentmanagement.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// Boot 4 package. On Boot 3 use:
// org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    private StudentResponse ana() {
        return new StudentResponse(1L, "Ana", "Santos", "ana@mail.com", LocalDate.of(2003, 5, 14));
    }

    @Test
    void getAllStudents_returns200AndList() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(ana()));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("ana@mail.com"));
    }

    @Test
    void getStudent_existing_returns200() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(ana());

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ana"));
    }

    @Test
    void getStudent_missing_returns404WithMessage() throws Exception {
        when(studentService.getStudentById(99L)).thenThrow(new StudentNotFoundException(99L));

        mockMvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Student not found with id: 99"));
    }

    @Test
    void getStudent_nonNumericId_returns400() throws Exception {
        mockMvc.perform(get("/api/students/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid value for parameter 'id'"));
    }

    @Test
    void createStudent_validBody_returns201WithLocation() throws Exception {
        when(studentService.createStudent(any(StudentRequest.class))).thenReturn(ana());

        String json = """
                {
                  "firstName": "Ana",
                  "lastName": "Santos",
                  "email": "ana@mail.com",
                  "dateOfBirth": "2003-05-14"
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/students/1"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createStudent_emptyBody_returns400WithFieldErrors() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.firstName").value("First name is required"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email is required"));

        verify(studentService, never()).createStudent(any(StudentRequest.class));
    }

    @Test
    void createStudent_duplicateEmail_returns409() throws Exception {
        when(studentService.createStudent(any(StudentRequest.class)))
                .thenThrow(new DuplicateEmailException("ana@mail.com"));

        String json = """
                {
                  "firstName": "Ana",
                  "lastName": "Santos",
                  "email": "ana@mail.com",
                  "dateOfBirth": "2003-05-14"
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void deleteStudent_returns204() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService).deleteStudent(1L);
    }
}