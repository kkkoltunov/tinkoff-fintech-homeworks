package ru.tinkoff.fintechspringrest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.fintechspringrest.ApplicationIntegrationTest;
import ru.tinkoff.fintechspringrest.model.Course;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CourseControllerAuthorizationTest extends ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void tearDown() {
        cleanAndMigrate();
    }

    @Test
    void createCourseUnauthorizedUser() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        this.mockMvc.perform(post("/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    void createCourseAuthorizedUser() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        this.mockMvc.perform(post("/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createCourseAuthorizedAdmin() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        this.mockMvc.perform(post("/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = {"INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)",
            "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)"})
    void addListStudentToCourseUnauthorizedUser() throws Exception {
        List<Pair<Integer, Integer>> listStudentCourse = new ArrayList<>(List.of(
                Pair.of(1, 1)
        ));

        this.mockMvc.perform(post("/courses/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listStudentCourse)
                                .replace("key", "first")
                                .replace("value", "second")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = {"INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)",
            "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)"})
    @WithMockUser()
    void addListStudentToCourseAuthorizedUser() throws Exception {
        List<Pair<Integer, Integer>> listStudentCourse = new ArrayList<>(List.of(
                Pair.of(1, 1)
        ));

        this.mockMvc.perform(post("/courses/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listStudentCourse)
                                .replace("key", "first")
                                .replace("value", "second")))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = {"INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)",
            "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)"})
    @WithMockUser(roles = {"ADMIN"})
    void addListStudentToCourseAuthorizedAdmin() throws Exception {
        List<Pair<Integer, Integer>> listStudentCourse = new ArrayList<>(List.of(
                Pair.of(1, 1)
        ));

        this.mockMvc.perform(post("/courses/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listStudentCourse)
                                .replace("key", "first")
                                .replace("value", "second")))
                .andExpect(status().isOk());
    }

    @Test
    void getCoursesUnauthorizedUser() throws Exception {
        this.mockMvc.perform(get("/courses/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    void getCoursesAuthorizedUser() throws Exception {
        this.mockMvc.perform(get("/courses/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getCoursesAuthorizedAdmin() throws Exception {
        this.mockMvc.perform(get("/courses/"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    void getCourseUnauthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/courses/" + id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    @WithMockUser()
    void getCourseAuthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/courses/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    @WithMockUser(roles = {"ADMIN"})
    void getCourseAuthorizedAdmin() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/courses/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    void updateCourseUnauthorizedUser() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("ne java")
                .setDescription("yze ne krasivo")
                .setReqiredGrade(1);

        this.mockMvc.perform(put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    @WithMockUser()
    void updateCourseAuthorizedUser() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("ne java")
                .setDescription("yze ne krasivo")
                .setReqiredGrade(1);

        this.mockMvc.perform(put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    @WithMockUser(roles = {"ADMIN"})
    void updateCourseAuthorizedAdmin() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("ne java")
                .setDescription("yze ne krasivo")
                .setReqiredGrade(1);

        this.mockMvc.perform(put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    void deleteCourseUnauthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete("/courses/" + id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    @WithMockUser()
    void deleteCourseAuthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete("/courses/" + id))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    @WithMockUser(roles = {"ADMIN"})
    void deleteCourseAuthorizedAdmin() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete("/courses/" + id))
                .andExpect(status().isOk());
    }
}