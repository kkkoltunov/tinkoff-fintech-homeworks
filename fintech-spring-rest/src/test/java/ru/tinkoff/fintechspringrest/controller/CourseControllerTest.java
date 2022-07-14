package ru.tinkoff.fintechspringrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.fintechspringrest.ApplicationIntegrationTest;
import ru.tinkoff.fintechspringrest.model.Course;
import ru.tinkoff.fintechspringrest.validator.ExceptionError;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CourseControllerTest extends ApplicationIntegrationTest {

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
    void createCourseSuccess() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        this.mockMvc.perform(post("/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk());

        Course expectedCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        Course actualCourse = jdbcTemplate.queryForObject("SELECT * FROM courses WHERE id=1",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void createCourseFailedIncorrectName() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        String expectedContent = "[\"Course name should not be empty!\"]";

        this.mockMvc.perform(post("/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedCountCourses = 0;

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCountCourses, actualListCourses.size());
    }

    @Test
    void createCourseFailedIncorrectDescription() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("java")
                .setDescription("")
                .setReqiredGrade(10);

        String expectedContent = "[\"Course description should not be empty!\"]";

        this.mockMvc.perform(post("/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedCountCourses = 0;

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCountCourses, actualListCourses.size());
    }

    @Test
    void createCourseFailedIncorrectMinGrade() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(-10);

        String expectedContent = "[\"Grade must be greater than 0!\"]";

        this.mockMvc.perform(post("/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedCountCourses = 0;

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCountCourses, actualListCourses.size());
    }

    @Test
    void createCourseFailedIncorrectMaxGrade() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(1000);

        String expectedContent = "[\"Grade must be less than 100!\"]";

        this.mockMvc.perform(post("/courses/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedCountCourses = 0;

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCountCourses, actualListCourses.size());
    }

    @Test
    @Sql(statements = {"INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)",
            "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)"})
    void addListStudentToCourseSuccess() throws Exception {
        List<Pair<Integer, Integer>> listStudentCourse = new ArrayList<>(List.of(
                Pair.of(1, 1)
        ));

        this.mockMvc.perform(post("/courses/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listStudentCourse)
                                .replace("key", "first")
                                .replace("value", "second")))
                .andExpect(status().isOk());

        Pair<Integer, Integer> expectedPair = Pair.of(1, 1);

        Pair<Integer, Integer> actualPair = jdbcTemplate.queryForObject("SELECT * FROM students_courses",
                (resultSet, rowNumber) -> Pair.of(resultSet.getInt(1), resultSet.getInt(2)));

        Assertions.assertEquals(expectedPair, actualPair);
    }

    @Test
    @Sql(statements = {"INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10), " +
            "(2, 'c++', 'poidet', 7)",
            "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15), (2, 'vasya', 23, 12)"})
    void addListStudentToCourseSuccessTwoStudents() throws Exception {
        List<Pair<Integer, Integer>> listStudentCourse = new ArrayList<>(List.of(
                Pair.of(1, 1),
                Pair.of(1, 2),
                Pair.of(2, 1)
        ));

        this.mockMvc.perform(post("/courses/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listStudentCourse)
                                .replace("key", "first")
                                .replace("value", "second")))
                .andExpect(status().isOk());

        List<Pair<Integer, Integer>> expectedListPairs = new ArrayList<>(List.of(
                Pair.of(1, 1),
                Pair.of(1, 2),
                Pair.of(2, 1)
        ));

        List<Pair<Integer, Integer>> actualListPairs = jdbcTemplate.query("SELECT * FROM students_courses",
                (resultSet, rowNumber) -> Pair.of(resultSet.getInt(1), resultSet.getInt(2)));

        Assertions.assertEquals(expectedListPairs, actualListPairs);
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    void addListStudentToCourseFailedStudentNotExist() throws Exception {
        List<Pair<Integer, Integer>> listStudentCourse = new ArrayList<>(List.of(
                Pair.of(1, 1)
        ));

        ExceptionError error = new ExceptionError("Student with id = 1 not found!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(post("/courses/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listStudentCourse)
                                .replace("key", "first")
                                .replace("value", "second")))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedContent));

        int expectedPairsCount = 0;

        List<Pair<Integer, Integer>> actualListPairs = jdbcTemplate.query("SELECT * FROM students_courses",
                (resultSet, rowNumber) -> Pair.of(resultSet.getInt(1), resultSet.getInt(2)));

        Assertions.assertEquals(expectedPairsCount, actualListPairs.size());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    void addListStudentToCourseFailedCourseNotExist() throws Exception {
        List<Pair<Integer, Integer>> listStudentCourse = new ArrayList<>(List.of(
                Pair.of(1, 1)
        ));

        ExceptionError error = new ExceptionError("Course with id = 1 not found!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(post("/courses/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listStudentCourse)
                                .replace("key", "first")
                                .replace("value", "second")))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedContent));

        int expectedPairsCount = 0;

        List<Pair<Integer, Integer>> actualListPairs = jdbcTemplate.query("SELECT * FROM students_courses",
                (resultSet, rowNumber) -> Pair.of(resultSet.getInt(1), resultSet.getInt(2)));

        Assertions.assertEquals(expectedPairsCount, actualListPairs.size());
    }

    @Test
    @Sql(statements = {"INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)",
            "INSERT INTO students (id, name, age, grade) VALUES (1, 'vasya', 23, 5)"})
    void addListStudentToCourseFailedIncorrectStudentGrade() throws Exception {
        List<Pair<Integer, Integer>> listStudentCourse = new ArrayList<>(List.of(
                Pair.of(1, 1)
        ));

        ExceptionError error = new ExceptionError("Failed to enroll a student in the course: " +
                "the student's grade must be >= requiredGrade of the course!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(post("/courses/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listStudentCourse)
                                .replace("key", "first")
                                .replace("value", "second")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedPairsCount = 0;

        List<Pair<Integer, Integer>> actualListPairs = jdbcTemplate.query("SELECT * FROM students_courses",
                (resultSet, rowNumber) -> Pair.of(resultSet.getInt(1), resultSet.getInt(2)));

        Assertions.assertEquals(expectedPairsCount, actualListPairs.size());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    void getCourseByIdSuccess() throws Exception {
        int id = 1;

        Course expectedCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        String expectedContent = objectMapper.writeValueAsString(expectedCourse);

        this.mockMvc.perform(get("/courses/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));

        Course actualCourse = jdbcTemplate.queryForObject("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void getCourseByIdFailed() throws Exception {
        int id = 1;

        ExceptionError error = new ExceptionError("Course with id = 1 not found!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(get("/courses/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedContent));

        int expectedCourseCount = 0;

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCourseCount, actualListCourses.size());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)," +
            "(2, 'c++', 'poidet', 7)")
    void getAllCourses() throws Exception {
        List<Course> courseList = new ArrayList<>(List.of(
                new Course()
                        .setId(1)
                        .setName("java")
                        .setDescription("krasivo")
                        .setReqiredGrade(10),
                new Course()
                        .setId(2)
                        .setName("c++")
                        .setDescription("poidet")
                        .setReqiredGrade(7)
        ));

        String expectedContent = objectMapper.writeValueAsString(courseList);

        this.mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));

        List<Course> expectedCourseList = new ArrayList<>(List.of(
                new Course()
                        .setId(1)
                        .setName("java")
                        .setDescription("krasivo")
                        .setReqiredGrade(10),
                new Course()
                        .setId(2)
                        .setName("c++")
                        .setDescription("poidet")
                        .setReqiredGrade(7)
        ));

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCourseList, actualListCourses);
    }

    @Test
    void getAllCoursesEmptyList() throws Exception {
        List<Course> courseList = new ArrayList<>();

        String expectedContent = objectMapper.writeValueAsString(courseList);

        this.mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));

        int expectedCoursesCount = 0;

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCoursesCount, actualListCourses.size());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    void updateCourseSuccess() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("ne java")
                .setDescription("yze ne krasivo")
                .setReqiredGrade(1);

        this.mockMvc.perform(put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk());

        Course expectedCourse = new Course()
                .setId(1)
                .setName("ne java")
                .setDescription("yze ne krasivo")
                .setReqiredGrade(1);

        Course actualCourse = jdbcTemplate.queryForObject("SELECT * FROM courses WHERE id=1",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void updateCourseFailedNotExist() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        ExceptionError error = new ExceptionError("Course with id = 1 not found!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedContent));

        int expectedCountCourses = 0;

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCountCourses, actualListCourses.size());
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    void updateCourseFailedIncorrectDescription() throws Exception {
        Course course = new Course()
                .setId(1)
                .setName("java")
                .setDescription("")
                .setReqiredGrade(10);

        String expectedContent = "[\"Course description should not be empty!\"]";

        this.mockMvc.perform(put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        Course expectedCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        Course actualCourse = jdbcTemplate.queryForObject("SELECT * FROM courses WHERE id=1",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCourse, actualCourse);
    }

    @Test
    @Sql(statements = "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)")
    void deleteCourseSuccess() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete("/courses/" + id))
                .andExpect(status().isOk());

        int expectedCountCourses = 0;

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCountCourses, actualListCourses.size());
    }

    @Test
    void deleteCourseFailedNotExist() throws Exception {
        int id = 1;

        ExceptionError error = new ExceptionError("Course with id = 1 not found!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(delete("/courses/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedContent));

        int expectedCountCourses = 0;

        List<Course> actualListCourses = jdbcTemplate.query("SELECT * FROM courses",
                BeanPropertyRowMapper.newInstance(Course.class));

        Assertions.assertEquals(expectedCountCourses, actualListCourses.size());
    }
}