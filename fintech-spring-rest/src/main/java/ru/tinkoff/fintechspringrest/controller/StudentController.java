package ru.tinkoff.fintechspringrest.controller;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintechspringrest.model.Student;
import ru.tinkoff.fintechspringrest.service.StudentService;
import ru.tinkoff.fintechspringrest.service.StudentsCoursesService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    private final StudentsCoursesService studentsCoursesService;

    @PostMapping
    public void createStudent(@Valid @RequestBody Student student) {
        studentService.save(student);
    }

    @GetMapping("/{id}/courses")
    public List<Integer> getCoursesIdByStudentId(@PathVariable("id") int id) {
        return studentsCoursesService.find(id);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable("id") int id) {
        return studentService.getById(id);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.findAll();
    }

    @PutMapping
    @Transactional
    public void updateStudent(@Valid @RequestBody Student student) {
        studentService.findById(student.getId());
        studentService.update(student);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteStudentById(@PathVariable("id") int id) {
        studentService.findById(id);
        studentService.delete(id);
    }
}
