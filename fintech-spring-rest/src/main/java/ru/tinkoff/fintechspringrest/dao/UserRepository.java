package ru.tinkoff.fintechspringrest.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.tinkoff.fintechspringrest.model.User;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface UserRepository {

    @Insert("INSERT INTO users (id, login, password) VALUES (#{id}, #{login}, #{password})")
    void save(User user);

    @Select("SELECT * FROM users WHERE login = #{username}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "login", property = "login"),
            @Result(column = "password", property = "password")
    })
    Optional<User> findByUsername(String username);

    @Select("SELECT * FROM users")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "login", property = "login"),
            @Result(column = "password", property = "password")
    })
    List<User> findAll();

    @Delete("DELETE FROM users WHERE id = #{id}")
    void delete(int id);
}
