package ru.tinkoff.fintechspring.student;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Student {
    private String name;
    private int age;
    private transient String id;
    private LocalTime lessonsStart;
    private LocalTime lessonsEnd;

    public static Student of(String name, int age, String lessonsStart, String lessonsEnd) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH");

        String lessonsStartString = lessonsStart.length() == 1 ? "0" + lessonsStart : lessonsStart;
        String lessonsEndString = lessonsEnd.length() == 1 ? "0" + lessonsEnd : lessonsEnd;

        return new Student(
                name,
                age,
                UUID.randomUUID().toString(),
                LocalTime.parse(lessonsStartString, dateTimeFormatter),
                LocalTime.parse(lessonsEndString, dateTimeFormatter)
        );
    }
}
