package com.scotiabank.students.service;

import com.scotiabank.students.exception.DuplicateStudentException;
import com.scotiabank.students.model.Student;
import com.scotiabank.students.repository.StudentRepository;
import com.scotiabank.students.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void shouldSaveStudentSuccessfully() {
        Student student = new Student("A001", "Juan", "Perez", "ACTIVE", 20);

        when(studentRepository.existsById("A001")).thenReturn(Mono.just(false));
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.just(student));

        StepVerifier.create(studentService.save(student))
                .expectNextMatches(s -> "A001".equals(s.getId()))
                .verifyComplete();
    }

    @Test
    void shouldThrowDuplicateExceptionWhenIdExists() {
        Student student = new Student("A001", "Juan", "Perez", "ACTIVE", 20);

        when(studentRepository.existsById("A001")).thenReturn(Mono.just(true));

        StepVerifier.create(studentService.save(student))
                .expectError(DuplicateStudentException.class)
                .verify();
    }

    @Test
    void shouldReturnActiveStudents() {
        Student active1 = new Student("A001", "Juan", "Perez", "ACTIVE", 20);
        Student active2 = new Student("A002", "Maria", "Lopez", "ACTIVE", 22);

        when(studentRepository.findByStatus("ACTIVE")).thenReturn(Flux.just(active1, active2));

        StepVerifier.create(studentService.findAllActive())
                .expectNextMatches(s -> "A001".equals(s.getId()))
                .expectNextMatches(s -> "A002".equals(s.getId()))
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNoActiveStudents() {
        when(studentRepository.findByStatus("ACTIVE")).thenReturn(Flux.empty());

        StepVerifier.create(studentService.findAllActive())
                .verifyComplete();
    }
}