package ru.tinkoff.fintechspringrest.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.tinkoff.fintechspringrest.model.Role;

import java.util.List;

@Mapper
@Repository
public interface RoleRepository {

    @Insert("INSERT INTO roles (id, name) VALUES (#{id}, #{name})")
    void save(Role role);

    @Select("SELECT * FROM roles")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name")
    })
    List<Role> findAll();

    @Insert("DELETE FROM roles WHERE id = {#id}")
    void delete(int id);
}
