package ru.tinkoff.fintechspringrest.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintechspringrest.model.Course;
import ru.tinkoff.fintechspringrest.model.Student;
import ru.tinkoff.fintechspringrest.service.CourseService;
import ru.tinkoff.fintechspringrest.service.StudentService;
import ru.tinkoff.fintechspringrest.service.StudentsCoursesService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final StudentService studentService;

    private final CourseService courseService;

    private final StudentsCoursesService studentsCoursesService;

    @PostMapping
    public void createCourse(@Valid @RequestBody Course course) {
        courseService.save(course);
    }

    @PostMapping("/students")
    public void addListStudentToCourse(@RequestBody List<Pair<Integer, Integer>> pairs) {
        for (var pair : pairs) {
            Student student = studentService.getById(pair.getFirst());
            Course course = courseService.getById(pair.getSecond());
            if (student.getGrade() < course.getReqiredGrade()) {
                throw new IllegalArgumentException("Failed to enroll a student in the course: " +
                        "the student's grade must be >= requiredGrade of the course!");
            }
        }
        studentsCoursesService.saveList(pairs);
    }

    @GetMapping("/{id}")
    public Course getCourseById(@PathVariable("id") int id) {
        return courseService.getById(id);
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAll();
    }

    @PutMapping
    @Transactional
    public void updateCourse(@Valid @RequestBody Course course) {
        courseService.findById(course.getId());
        courseService.update(course);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteCourseById(@PathVariable("id") int id) {
        courseService.findById(id);
        courseService.delete(id);
    }
}