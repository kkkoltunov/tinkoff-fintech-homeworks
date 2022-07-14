package ru.tinkoff.fintechspringdb.service;

import lombok.AllArgsConstructor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.fintechspringdb.dao.StudentRepository;
import ru.tinkoff.fintechspringdb.dao.StudentsCoursesRepository;
import ru.tinkoff.fintechspringdb.model.Student;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentsCoursesService {

    private final StudentsCoursesRepository studentsCoursesRepository;

    private final StudentRepository studentRepository;

    private final SqlSessionFactory sqlSessionFactory;

    @Transactional(rollbackFor = Exception.class)
    public void save(Student student) {
        studentRepository.save(student);

        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            student.getCoursesId().forEach(value -> studentsCoursesRepository.save(student.getId(), value));
            sqlSession.commit();
        }
    }

    public Student find(int id) {
        Student student = studentRepository.findById(id).orElseThrow();
        student.setCoursesId(studentsCoursesRepository.find(id));
        return student;
    }

    public List<Integer> findByIdStudent(int id) {
        return studentsCoursesRepository.find(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Student student) {
        studentRepository.update(student);

        List<Integer> courses = studentsCoursesRepository.find(student.getId());

        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            student.getCoursesId().stream()
                    .filter(courseId -> !courses.contains(courseId))
                    .forEach(value -> studentsCoursesRepository.save(student.getId(), value));
            sqlSession.commit();
        }

        courses.stream()
                .filter(courseId -> !student.getCoursesId().contains(courseId))
                .forEach(value -> studentsCoursesRepository.delete(student.getId(), value));
    }
}
