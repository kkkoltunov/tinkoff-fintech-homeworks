package ru.tinkoff.fintechspringrest.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.jdbc.Sql;
import ru.tinkoff.fintechspringrest.dao.CourseRepository;
import ru.tinkoff.fintechspringrest.model.Course;
import ru.tinkoff.fintechspringrest.service.CourseService;

import java.util.Optional;

class CourseCacheTest {

    private CourseService courseService;
    private CourseRepository courseRepository;
    private CourseCache courseCache;

    @BeforeEach
    public void setUp() {
        courseRepository = Mockito.mock(CourseRepository.class);
        courseCache = new CourseCache();
        courseService = new CourseService(courseRepository, courseCache);
    }

    @Test
    void createCourseWithoutCache() {
        int id = 1;

        Course testCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        Mockito.doNothing().when(courseRepository).save(testCourse);
    }

    @Test
    void getCourseWithoutCache() {
        int id = 1;

        Course testCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        Mockito.when(courseRepository.findById(id))
                .thenReturn(Optional.of(testCourse));

        Course expectedCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        Course actualCourse = courseService.getById(id);
        Assertions.assertEquals(expectedCourse, actualCourse);

        Mockito.doThrow(new RuntimeException("Обратились к бд!")).when(courseRepository).findById(id);
        Course cachedCourse = courseCache.createCache(id, (id1) -> courseService.findById(id));
        Assertions.assertEquals(expectedCourse, cachedCourse);
    }

    @Test
    void getCourseWithCache() {
        int id = 1;
        Course testCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        Mockito.doThrow(new RuntimeException("Обратились к бд!")).when(courseRepository).findById(id);

        Course expectedCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        Course cachedCourse = courseService.getById(id);
        Assertions.assertEquals(expectedCourse, cachedCourse);

        Mockito.doThrow(new RuntimeException("Обратились к бд!")).when(courseRepository).findById(id);
        Course cachedCourse1 = courseService.getById(id);
        Assertions.assertEquals(expectedCourse, cachedCourse1);
    }

    @Test
    void updateCourseWithCache() {
        int id = 1;

        Course testCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        Mockito.when(courseRepository.findById(id))
                .thenReturn(Optional.of(testCourse));
        courseService.getById(id);

        Mockito.doNothing().when(courseRepository).update(testCourse);
        courseService.update(testCourse);

        Mockito.when(courseRepository.findById(id))
                .thenReturn(Optional.of(testCourse));

        Mockito.doThrow(new RuntimeException("Обратились к бд!")).when(courseRepository).findById(id);
        Assertions.assertThrows(RuntimeException.class, () -> courseService.getById(id));
    }

    @Test
    void deleteCourseWithCache() {
        int id = 1;

        Course testCourse = new Course()
                .setId(1)
                .setName("java")
                .setDescription("krasivo")
                .setReqiredGrade(10);

        Mockito.when(courseRepository.findById(id))
                .thenReturn(Optional.of(testCourse));
        courseService.getById(id);

        Mockito.doNothing().when(courseRepository).delete(id);
        courseService.delete(id);

        Mockito.when(courseRepository.findById(id))
                .thenReturn(Optional.of(testCourse));

        Mockito.doThrow(new RuntimeException("Обратились к бд!")).when(courseRepository).findById(id);
        Assertions.assertThrows(RuntimeException.class, () -> courseService.getById(id));
    }
}