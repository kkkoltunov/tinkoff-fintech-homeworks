package ru.tinkoff.fintechspringrest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.fintechspringrest.ApplicationIntegrationTest;
import ru.tinkoff.fintechspringrest.model.Course;
import ru.tinkoff.fintechspringrest.model.Student;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class StudentControllerAuthorizationTest extends ApplicationIntegrationTest {

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
    void createStudentUnauthorizedUser() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(10);

        this.mockMvc.perform(post("/students/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    void createStudentAuthorizedUser() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(10);

        this.mockMvc.perform(post("/students/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void createStudentAuthorizedAdmin() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("kirill")
                .setAge(18)
                .setGrade(10);

        this.mockMvc.perform(post("/students/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());
    }

    @Test
    void getStudentsUnauthorizedUser() throws Exception {
        this.mockMvc.perform(get("/students/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser()
    void getStudentsAuthorizedUser() throws Exception {
        this.mockMvc.perform(get("/students/"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getStudentsAuthorizedAdmin() throws Exception {
        this.mockMvc.perform(get("/students/"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    void getStudentUnauthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/students/" + id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    @WithMockUser()
    void getStudentAuthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/students/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    @WithMockUser(roles = {"ADMIN"})
    void getStudentAuthorizedAdmin() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/students/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = {"INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)",
            "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)",
            "INSERT INTO students_courses (student_id, course_id) VALUES (1, 1)"})
    void getCoursesIdByStudentIdUnauthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/students/" + id + "/courses"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = {"INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)",
            "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)",
            "INSERT INTO students_courses (student_id, course_id) VALUES (1, 1)"})
    @WithMockUser()
    void getCoursesIdByStudentIdAuthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/students/" + id + "/courses"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = {"INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)",
            "INSERT INTO courses (id, name, description, reqired_grade) VALUES (1, 'java', 'krasivo', 10)",
            "INSERT INTO students_courses (student_id, course_id) VALUES (1, 1)"})
    @WithMockUser(roles = {"ADMIN"})
    void getCoursesIdByStudentIdAuthorizedAdmin() throws Exception {
        int id = 1;

        this.mockMvc.perform(get("/students/" + id + "/courses"))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    void updateStudentUnauthorizedUser() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("ne kirill")
                .setAge(38)
                .setGrade(5);

        this.mockMvc.perform(put("/students/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    @WithMockUser()
    void updateStudentAuthorizedUser() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("ne kirill")
                .setAge(38)
                .setGrade(5);

        this.mockMvc.perform(put("/students/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    @WithMockUser(roles = {"ADMIN"})
    void updateStudentAuthorizedAdmin() throws Exception {
        Student student = new Student()
                .setId(1)
                .setName("ne kirill")
                .setAge(38)
                .setGrade(5);

        this.mockMvc.perform(put("/students/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    void deleteStudentUnauthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete("/students/" + id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    @WithMockUser()
    void deleteStudentAuthorizedUser() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete("/students/" + id))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(statements = "INSERT INTO students (id, name, age, grade) VALUES (1, 'kirill', 18, 15)")
    @WithMockUser(roles = {"ADMIN"})
    void deleteStudentAuthorizedAdmin() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete("/students/" + id))
                .andExpect(status().isOk());
    }
}