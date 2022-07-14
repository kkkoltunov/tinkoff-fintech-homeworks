package ru.tinkoff.fintechspringrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Role {
    private int id;

    @NotBlank(message = "Role name should not be empty!")
    @Size(min = 4, max = 16, message = "Name length should be in the range from 4 to 16!")
    private String name;
}
