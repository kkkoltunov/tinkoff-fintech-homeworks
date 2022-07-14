package ru.tinkoff.fintechspringrest.validator;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ExceptionError {

    private String message;
}
