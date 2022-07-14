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
public class User {
    private int id;

    @NotBlank(message = "Role name should not be empty!")
    @Size(min = 2, max = 32, message = "Login length should be in the range from 2 to 32!")
    private String login;

    @NotBlank(message = "Password should not be empty!")
    @Size(min = 8, max = 32, message = "Password length should be in the range from 8 to 32!")
    private String password;
}
