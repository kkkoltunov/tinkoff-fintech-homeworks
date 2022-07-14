package ru.tinkoff.fintechspringrest.validator;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import ru.tinkoff.fintechspringrest.service.CourseService;
import ru.tinkoff.fintechspringrest.service.StudentService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
@AllArgsConstructor
public class GradeValidator implements ConstraintValidator<GradeValidation, List<Pair<Integer, Integer>>> {

    private final CourseService courseService;

    private final StudentService studentService;

    @Override
    public boolean isValid(List<Pair<Integer, Integer>> pairs, ConstraintValidatorContext constraintValidatorContext) {
        for (Pair<Integer, Integer> pair : pairs) {
            int reqiredGradeCourse = courseService.getById(pair.getSecond()).getReqiredGrade();
            int gradeStudent = studentService.getById(pair.getFirst()).getGrade();

            if (gradeStudent < reqiredGradeCourse) {
                return false;
            }
        }
        return true;
    }
}