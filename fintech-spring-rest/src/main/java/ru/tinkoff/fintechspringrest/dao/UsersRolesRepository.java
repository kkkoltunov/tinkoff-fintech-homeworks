package ru.tinkoff.fintechspringrest.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UsersRolesRepository {

    @Insert("INSERT INTO users_roles (user_id, role_id) VALUES (#{userId}, #{roleId})")
    void save(int userId, int roleId);

    @Select("SELECT role_id FROM users_roles WHERE user_id = #{userId}")
    @ResultType(Integer.class)
    List<Integer> find(int userId);
}
