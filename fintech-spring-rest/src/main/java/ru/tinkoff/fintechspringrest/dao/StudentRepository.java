package ru.tinkoff.fintechspringrest.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.tinkoff.fintechspringrest.model.Student;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface StudentRepository {

    @Insert("INSERT INTO students (id, name, age, grade) VALUES (#{id}, #{name}, #{age}, #{grade})")
    void save(Student student);

    @Select("SELECT * FROM students WHERE id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "age", property = "age"),
            @Result(column = "grade", property = "grade")
    })
    Optional<Student> findById(int id);

    @Select("SELECT * FROM students")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "age", property = "age"),
            @Result(column = "grade", property = "grade")
    })
    List<Student> findAll();

    @Update("UPDATE students SET name = #{name}, age = #{age}, grade = #{grade} WHERE id = #{id}")
    void update(Student student);

    @Delete("DELETE FROM students WHERE id = #{id}")
    void delete(int id);
}
