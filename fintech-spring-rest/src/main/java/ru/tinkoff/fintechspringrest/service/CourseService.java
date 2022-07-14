package ru.tinkoff.fintechspringrest.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintechspringrest.cache.CourseCache;
import ru.tinkoff.fintechspringrest.dao.CourseRepository;
import ru.tinkoff.fintechspringrest.model.Course;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository repository;

    private final CourseCache courseCache;

    public void save(Course course) {
        repository.save(course);
    }

    public Course findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id = " + id + " not found!"));
    }

    public Course getById(int id) {
        return courseCache.createCache(id, this::findById);
    }

    public List<Course> findAll() {
        return repository.findAll();
    }

    public void update(Course course) {
        synchronized (this) {
            repository.update(course);
            courseCache.deleteCache(course.getId());
        }
    }

    public void delete(int id) {
        synchronized (this) {
            repository.delete(id);
            courseCache.deleteCache(id);
        }
    }
}