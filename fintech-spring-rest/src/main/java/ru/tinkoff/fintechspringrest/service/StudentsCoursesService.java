package ru.tinkoff.fintechspringrest.service;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.fintechspringrest.dao.StudentsCoursesRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentsCoursesService {

    private final StudentsCoursesRepository studentsCoursesRepository;

    private final StudentService studentService;

    private final CourseService courseService;

    private final SqlSessionFactory sqlSessionFactory;

    @Transactional
    public void saveList(List<Pair<Integer, Integer>> pairs) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            for (var pair : pairs) {
                studentService.getById(pair.getFirst());
                courseService.getById(pair.getSecond());
                studentsCoursesRepository.save(pair.getFirst(), pair.getSecond());
            }
            sqlSession.commit();
        }
    }

    public List<Integer> find(int studentId) {
        studentService.getById(studentId);
        return studentsCoursesRepository.find(studentId);
    }
}
