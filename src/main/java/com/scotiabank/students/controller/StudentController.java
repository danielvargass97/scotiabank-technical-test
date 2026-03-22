package com.scotiabank.students.controller;

import com.scotiabank.students.dto.StudentRequest;
import com.scotiabank.students.model.Student;
import com.scotiabank.students.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Student> save(@Valid @RequestBody StudentRequest request) {
        Student student = new Student(
                request.getId(),
                request.getFirstName(),
                request.getLastName(),
                request.getStatus(),
                request.getAge()
        );
        return studentService.save(student);
    }

    @GetMapping("/active")
    public Flux<Student> findAllActive() {
        return studentService.findAllActive();
    }
}