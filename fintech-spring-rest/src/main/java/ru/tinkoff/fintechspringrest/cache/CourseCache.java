package ru.tinkoff.fintechspringrest.cache;

import org.springframework.stereotype.Component;
import ru.tinkoff.fintechspringrest.model.Course;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class CourseCache {

    private final ConcurrentHashMap<Integer, Course> cachedCourseMap;

    public CourseCache() {
        cachedCourseMap = new ConcurrentHashMap<>();
    }

    public Course createCache(Integer id, Function<Integer, Course> function) {
        return cachedCourseMap.computeIfAbsent(id, function);
    }

    public void deleteCache(int id) {
        cachedCourseMap.remove(id);
    }
}