package ru.tinkoff.fintechspringrest.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudentsCoursesRepository {

    @Insert("INSERT INTO students_courses (student_id, course_id) VALUES (#{studentId}, #{courseId})")
    void save(int studentId, int courseId);

    @Select("SELECT course_id FROM students_courses WHERE student_id = #{studentId}")
    @ResultType(Integer.class)
    List<Integer> find(int studentId);
}
