package ru.tinkoff.fintechspringrest.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import ru.tinkoff.fintechspringrest.event.StudentEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
@Repository
public interface StudentEventRepository {

    @Insert("INSERT INTO students_events (id, student_id, status) VALUES (#{id}, #{studentId}, #{status})")
    void save(StudentEvent studentEvent);

    @Select("SELECT * FROM students_events WHERE id = #{id}")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "student_id", property = "studentId"),
            @Result(column = "status", property = "status")
    })
    Optional<StudentEvent> findById(long id);

    @Select("SELECT * FROM students_events")
    @Results(value = {
            @Result(column = "id", property = "id"),
            @Result(column = "student_id", property = "studentId"),
            @Result(column = "status", property = "status")
    })
    List<StudentEvent> findAll();


    @Update("<script> " +
                "DELETE FROM students_events " +
                    "<where> id IN (" +
                        "<foreach item = 'item' index = 'index' collection = 'list' " +
                            "separator = ','> " +
                            "#{item}::uuid " +
                        "</foreach>) " +
                    "</where> " +
            "</script>")
    void deleteByListId(@Param("list") List<UUID> uuidList);
}
