package ru.tinkoff.fintechspringrest.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.tinkoff.fintechspringrest.model.Course;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface CourseRepository {

    @Insert("INSERT INTO courses (id, name, description, reqired_grade) VALUES (#{id}, #{name}, #{description}, #{reqiredGrade})")
    void save(Course course);

    @Select("SELECT * FROM courses WHERE id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "description", property = "description"),
            @Result(column = "reqired_grade", property = "reqiredGrade")
    })
    Optional<Course> findById(int id);

    @Select("SELECT * FROM courses")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "description", property = "description"),
            @Result(column = "reqired_grade", property = "reqiredGrade")
    })
    List<Course> findAll();

    @Update("UPDATE courses SET name = #{name}, description = #{description}, reqired_grade = #{reqiredGrade} WHERE id = #{id}")
    void update(Course course);

    @Delete("DELETE FROM courses WHERE id = #{id}")
    void delete(int id);
}
