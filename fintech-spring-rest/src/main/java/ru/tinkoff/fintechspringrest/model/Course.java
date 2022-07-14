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
public class Course {
    private int id;

    @NotBlank(message = "Course name should not be empty!")
    private String name;

    @NotBlank(message = "Course description should not be empty!")
    private String description;

    @Min(value = 1, message = "Grade must be greater than 0!")
    @Max(value = 99, message = "Grade must be less than 100!")
    private int reqiredGrade;
}
