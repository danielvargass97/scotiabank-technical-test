package com.scotiabank.students.repository;

import com.scotiabank.students.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll().block();
    }

    @Test
    void shouldSaveAndFindActiveStudents() {
        Student active = new Student("A001", "Juan", "Perez", "ACTIVE", 20);
        Student inactive = new Student("A002", "Maria", "Lopez", "INACTIVE", 22);

        studentRepository.save(active).block();
        studentRepository.save(inactive).block();

        StepVerifier.create(studentRepository.findByStatus("ACTIVE"))
                .expectNextMatches(s -> "A001".equals(s.getId()))
                .verifyComplete();
    }

    @Test
    void shouldReturnTrueWhenStudentExists() {
        Student student = new Student("A001", "Juan", "Perez", "ACTIVE", 20);
        studentRepository.save(student).block();

        StepVerifier.create(studentRepository.existsById("A001"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void shouldReturnFalseWhenStudentDoesNotExist() {
        StepVerifier.create(studentRepository.existsById("NONEXISTENT"))
                .expectNext(false)
                .verifyComplete();
    }
}