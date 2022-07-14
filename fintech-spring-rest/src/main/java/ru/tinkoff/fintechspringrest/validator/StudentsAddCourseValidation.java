package ru.tinkoff.fintechspringrest.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class StudentsAddCourseValidation {

    @GradeValidation
    public List<Pair<Integer, Integer>> pairsStudentCourse;
}
