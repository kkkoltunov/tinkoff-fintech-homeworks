package ru.tinkoff.fintechspringdb.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.tinkoff.fintechspringdb.model.Course;

import java.util.List;

@Mapper
@Repository
public interface CourseRepository {

    @Insert("INSERT INTO courses (id, name, description) VALUES (#{id}, #{name}, #{description})")
    void save(Course course);

    @Select("SELECT id, name, description FROM courses WHERE id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "description", property = "description")
    })
    Course findById(int id);

    @Select("SELECT * FROM courses WHERE id = (SELECT TOP 1 course_id as id " +
            "FROM (SELECT students_courses.course_id, students.age " +
            "FROM students_courses " +
            "INNER JOIN students " +
            "ON students.id = students_courses.student_id) " +
            "GROUP BY course_id " +
            "ORDER BY AVG (AGE) DESC)")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(property = "description", column = "description")
    })
    Course findTopAvgAgeAmongStudents();

    @Select("SELECT id, name, description FROM courses")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(property = "description", column = "description")
    })
    List<Course> findAll();

    @Update("UPDATE courses SET name = #{name}, description = #{description} WHERE id = #{id}")
    void update(Course course);

    @Delete("DELETE FROM courses WHERE id = #{id}")
    void delete(int id);
}
