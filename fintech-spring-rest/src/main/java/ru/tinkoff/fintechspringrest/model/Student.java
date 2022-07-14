package ru.tinkoff.fintechspringrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Student {
    private int id;

    @NotBlank(message = "Student name should not be empty!")
    private String name;

    @Min(value = 6, message = "Age must be greater than 5!")
    @Max(value = 124, message = "Age must be less than 125!")
    private int age;

    @Min(value = 1, message = "Grade must be greater than 0!")
    @Max(value = 99, message = "Grade must be less than 100!")
    private int grade;
}
