package ru.tinkoff.fintechspringdb.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudentsCoursesRepository {

    @Insert("INSERT INTO students_courses (student_id, course_id) VALUES (#{studentId}, #{courseId})")
    void save(int studentId, int courseId);

    @Select("SELECT course_id FROM students_courses WHERE student_id = #{id}")
    @ResultType(Integer.class)
    List<Integer> find(int id);

    @Delete("DELETE FROM students_courses WHERE student_id = #{studentId} AND course_id = #{courseId}")
    void delete(int studentId, int courseId);
}
