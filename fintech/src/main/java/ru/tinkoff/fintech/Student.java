package ru.tinkoff.fintech;

import lombok.Data;

import java.util.UUID;

@Data
public class Student {
    private String name;
    private int age;
    private transient String id;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        id = UUID.randomUUID().toString();
    }
}
