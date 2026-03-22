package com.scotiabank.students.service.impl;

import com.scotiabank.students.exception.DuplicateStudentException;
import com.scotiabank.students.model.Student;
import com.scotiabank.students.repository.StudentRepository;
import com.scotiabank.students.service.StudentService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Mono<Student> save(Student student) {
        return studentRepository.existsById(student.getId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateStudentException(
                                "Student with id " + student.getId() + " already exists"
                        ));
                    }
                    return studentRepository.save(student);
                });
    }

    @Override
    public Flux<Student> findAllActive() {
        return studentRepository.findByStatus("ACTIVE");
    }
}