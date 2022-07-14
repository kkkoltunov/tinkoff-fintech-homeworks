package ru.tinkoff.fintechspringrest.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GradeValidator.class)
public @interface GradeValidation {

    public String message() default "Невозможно записать студента на курс: grade студента должен быть >= requiredGrade курса!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}