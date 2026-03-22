package com.scotiabank.students.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("student")
public class Student implements Persistable<String> {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String status;
    private Integer age;

    @Transient
    private boolean isNew = true;

    public Student(String id, String firstName, String lastName, String status, Integer age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.age = age;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}