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
import ru.tinkoff.fintechspringrest.model.Student;
import ru.tinkoff.fintechspringrest.validator.ExceptionError;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class StudentControllerTest extends ApplicationIntegrationTest {

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
    void createStudentSuccess() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(10);

        this.mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

        Student expectedStudent = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(10);

        Student actualStudent = jdbcTemplate.queryForObject("SELECT * FROM students WHERE id=1",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void createStudentFailedIncorrectName() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("")
                .setAge(18)
                .setGrade(10);

        String expectedContent = "[\"Student name should not be empty!\"]";

        this.mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedStudentsCount = 0;

        List<Student> actualListStudents = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudentsCount, actualListStudents.size());
    }

    @Test
    void createStudentFailedIncorrectMinAge() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(-18)
                .setGrade(10);

        String expectedContent = "[\"Age must be greater than 5!\"]";

        this.mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedStudentsCount = 0;

        List<Student> actualListStudents = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudentsCount, actualListStudents.size());
    }

    @Test
    void createStudentFailedIncorrectMaxAge() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(188)
                .setGrade(10);

        String expectedContent = "[\"Age must be less than 125!\"]";

        this.mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedStudentsCount = 0;

        List<Student> actualListStudents = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudentsCount, actualListStudents.size());
    }

    @Test
    void createStudentFailedIncorrectMinGrade() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(-10);

        String expectedContent = "[\"Grade must be greater than 0!\"]";

        this.mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedStudentsCount = 0;

        List<Student> actualListStudents = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudentsCount, actualListStudents.size());
    }

    @Test
    void createStudentFailedIncorrectMaxGrade() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(1000);

        String expectedContent = "[\"Grade must be less than 100!\"]";

        this.mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        int expectedStudentsCount = 0;

        List<Student> actualListStudents = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudentsCount, actualListStudents.size());
    }

    @Test
    @Sql(statements = {"INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)",
            "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)," +
                    "(2, 'c++', 'poidet', 7)",
            "INSERT INTO students_courses (student_id, course_id) VALUES (1, 1), (1, 2)"})
    void getCoursesIdByStudentIdSuccess() throws Exception {
        int id = 1;

        List<Integer> coursesList = new ArrayList<>(List.of(1, 2));

        String expectedContent = objectMapper.writeValueAsString(coursesList);

        this.mockMvc.perform(get("/students/" + id + "/courses"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));

        List<Pair<Integer, Integer>> expectedPairsList = new ArrayList<>(List.of(
                Pair.of(1, 1),
                Pair.of(1, 2)
        ));

        List<Pair<Integer, Integer>> actualPairs = jdbcTemplate.query("SELECT * FROM students_courses",
                (resultSet, rowNumber) -> Pair.of(resultSet.getInt(1), resultSet.getInt(2)));

        Assertions.assertEquals(expectedPairsList, actualPairs);
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    void getCoursesIdByStudentIdSuccessEmptyList() throws Exception {
        int id = 1;

        List<Integer> coursesList = new ArrayList<>(List.of());

        String expectedContent = objectMapper.writeValueAsString(coursesList);

        this.mockMvc.perform(get("/students/" + id + "/courses"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));

        int expectedPairsCount = 0;

        List<Pair<Integer, Integer>> actualPairs = jdbcTemplate.query("SELECT * FROM students_courses",
                (resultSet, rowNumber) -> Pair.of(resultSet.getInt(1), resultSet.getInt(2)));

        Assertions.assertEquals(expectedPairsCount, actualPairs.size());
    }

    @Test
    void getCoursesIdByStudentIdFailedNotExistStudent() throws Exception {
        int id = 1;

        ExceptionError error = new ExceptionError("Student with id = 1 not found!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(get("/students/" + id + "/courses"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedContent));

        int expectedPairsCount = 0;

        List<Pair<Integer, Integer>> actualPairs = jdbcTemplate.query("SELECT * FROM students_courses",
                (resultSet, rowNumber) -> Pair.of(resultSet.getInt(1), resultSet.getInt(2)));

        Assertions.assertEquals(expectedPairsCount, actualPairs.size());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    void getStudentByIdSuccess() throws Exception {
        int id = 1;

        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(15);

        String expectedContent = objectMapper.writeValueAsString(student);

        this.mockMvc.perform(get("/students/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));

        Student expectedStudent = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(15);

        Student actualStudent = jdbcTemplate.queryForObject("SELECT * FROM students WHERE id=1",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudent, actualStudent);
    }

    @Test
    void getStudentByIdFailed() throws Exception {
        int id = 1;

        ExceptionError error = new ExceptionError("Student with id = 1 not found!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(get("/students/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedContent));

        int expectedStudentsCount = 0;

        List<Student> actualListStudents = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudentsCount, actualListStudents.size());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15), (2, 'vasya', 28, 25)")
    void getAllStudents() throws Exception {
        List<Student> studentList = new ArrayList<>(List.of(
                new Student()
                        .setId(1)
                        .setName("kirill")
                        .setAge(18)
                        .setGrade(15),
                new Student()
                        .setId(2)
                        .setName("vasya")
                        .setAge(28)
                        .setGrade(25)
        ));

        String expectedContent = objectMapper.writeValueAsString(studentList);

        this.mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));

        List<Student> expectedStudentList = new ArrayList<>(List.of(
                new Student()
                        .setId(1)
                        .setName("kirill")
                        .setAge(18)
                        .setGrade(15),
                new Student()
                        .setId(2)
                        .setName("vasya")
                        .setAge(28)
                        .setGrade(25)
        ));

        List<Student> actualListStudents = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudentList, actualListStudents);
    }

    @Test
    void getAllStudentsEmptyList() throws Exception {
        List<Student> studentList = new ArrayList<>();

        String expectedContent = objectMapper.writeValueAsString(studentList);

        this.mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));

        int expectedStudentsCount = 0;

        List<Student> actualListStudents = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudentsCount, actualListStudents.size());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    void updateStudentsSuccess() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("ne kirill")
                .setAge(38)
                .setGrade(5);

        this.mockMvc.perform(put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

        Student expectedCourse = new Student()
                .setId(1)
                .setName("ne kirill")
                .setAge(38)
                .setGrade(5);

        Student actualStudent = jdbcTemplate.queryForObject("SELECT * FROM students WHERE id=1",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedCourse, actualStudent);
    }

    @Test
    void updateStudentFailedNotExist() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(15);

        ExceptionError error = new ExceptionError("Failed to update the student with id = 1!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedContent));

        int expectedCountStudents = 0;

        List<Student> actualListStudent = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedCountStudents, actualListStudent.size());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    void updateStudentFailedIncorrectDescription() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("")
                .setAge(18)
                .setGrade(15);

        String expectedContent = "[\"Student name should not be empty!\"]";

        this.mockMvc.perform(put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedContent));

        Student expectedStudent = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(15);

        Student actualStudent = jdbcTemplate.queryForObject("SELECT * FROM students WHERE id=1",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedStudent, actualStudent);
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    void deleteStudentSuccess() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete("/students/" + id))
                .andExpect(status().isOk());

        int expectedCountStudents = 0;

        List<Student> actualListStudent = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedCountStudents, actualListStudent.size());
    }

    @Test
    void deleteStudentFailedNotExist() throws Exception {
        int id = 1;

        ExceptionError error = new ExceptionError("Failed to delete the student with id = 1!");
        String expectedContent = objectMapper.writeValueAsString(error);

        this.mockMvc.perform(delete("/students/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedContent));

        int expectedCountStudents = 0;

        List<Student> actualListStudent = jdbcTemplate.query("SELECT * FROM students",
                BeanPropertyRowMapper.newInstance(Student.class));

        Assertions.assertEquals(expectedCountStudents, actualListStudent.size());
    }
}