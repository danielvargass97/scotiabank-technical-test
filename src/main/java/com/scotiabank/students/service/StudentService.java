package com.scotiabank.students.service;

import com.scotiabank.students.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {

    Mono<Student> save(Student student);
    Flux<Student> findAllActive();
}