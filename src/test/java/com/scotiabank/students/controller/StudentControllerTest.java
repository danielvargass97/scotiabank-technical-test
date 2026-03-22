package com.scotiabank.students.controller;

import com.scotiabank.students.exception.DuplicateStudentException;
import com.scotiabank.students.model.Student;
import com.scotiabank.students.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private StudentService studentService;

    @Test
    void shouldCreateStudentSuccessfully() {
        Student student = new Student("A001", "Juan", "Perez", "ACTIVE", 20);

        when(studentService.save(any(Student.class))).thenReturn(Mono.just(student));

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "id": "A001",
                            "firstName": "Juan",
                            "lastName": "Perez",
                            "status": "ACTIVE",
                            "age": 20
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("A001");
    }

    @Test
    void shouldReturn409WhenIdAlreadyExists() {
        when(studentService.save(any(Student.class)))
                .thenReturn(Mono.error(new DuplicateStudentException("Student with id A001 already exists")));

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "id": "A001",
                            "firstName": "Juan",
                            "lastName": "Perez",
                            "status": "ACTIVE",
                            "age": 20
                        }
                        """)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.error").isEqualTo("Student with id A001 already exists");
    }

    @Test
    void shouldReturn400WhenValidationFails() {
        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "id": "",
                            "firstName": "",
                            "lastName": "Perez",
                            "status": "ACTIVE",
                            "age": 20
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturnActiveStudents() {
        Student active1 = new Student("A001", "Juan", "Perez", "ACTIVE", 20);
        Student active2 = new Student("A002", "Maria", "Lopez", "ACTIVE", 22);

        when(studentService.findAllActive()).thenReturn(Flux.just(active1, active2));

        webTestClient.get()
                .uri("/api/students/active")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Student.class)
                .hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveStudents() {
        when(studentService.findAllActive()).thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/students/active")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Student.class)
                .hasSize(0);
    }
}