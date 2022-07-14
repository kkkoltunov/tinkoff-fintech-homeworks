package ru.tinkoff.fintechspringdb.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.tinkoff.fintechspringdb.model.Student;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface StudentRepository {

    @Insert("INSERT INTO students (id, name, age) VALUES (#{id}, #{name}, #{age})")
    void save(Student student);

    @Select("SELECT id, name, age FROM students WHERE id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "age", property = "age")
    })
    Optional<Student> findById(int id);

    @Select("SELECT id, name, age FROM students")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "age", property = "age")
    })
    List<Student> findAll();

    @Update("UPDATE students SET name = #{name}, age = #{age} WHERE id = #{id}")
    void update(Student student);

    @Delete("DELETE FROM students WHERE id = #{id}")
    void delete(int id);
}
