package ru.tinkoff.fintechspringdb.service;

import org.springframework.stereotype.Service;
import ru.tinkoff.fintechspringdb.dao.CourseRepository;
import ru.tinkoff.fintechspringdb.model.Course;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public void save(Course course) {
        repository.save(course);
    }

    public Course findById(int id) {
        return repository.findById(id);
    }

    public Course findTopAvgAgeAmongStudents() {
        return repository.findTopAvgAgeAmongStudents();
    }

    public List<Course> findAll() {
        return repository.findAll();
    }

    public void update(Course course) {
        repository.update(course);
    }

    public void delete(int id) {
        repository.delete(id);
    }
}
