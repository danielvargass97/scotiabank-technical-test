package com.scotiabank.students.repository;

import com.scotiabank.students.model.Student;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StudentRepository extends ReactiveCrudRepository<Student, String> {

    Flux<Student> findByStatus(String status);
}