package ru.tinkoff.fintechspringdb;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.tinkoff.fintechspringdb.model.Course;
import ru.tinkoff.fintechspringdb.model.Student;
import ru.tinkoff.fintechspringdb.service.CourseService;
import ru.tinkoff.fintechspringdb.service.StudentService;
import ru.tinkoff.fintechspringdb.service.StudentsCoursesService;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FintechSpringDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintechSpringDbApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(StudentService studentService, CourseService courseService,
                                        StudentsCoursesService studentsCoursesService) {
        return args -> {
            // CRUD для Course
            CRUDCourse(courseService);

            // CRUD для Student
            CRUDStudent(studentService, courseService, studentsCoursesService);

            // Поиск курса с самым высоким средним возрастом учеников
            findTopAverageAgeAmongStudents(studentService, courseService, studentsCoursesService);

            // Полноценное обновление студента с изменением курсов.
            updateStudent(studentsCoursesService);

            // Проверка на откат транзакции при добавлении студента с несуществующим курсом.
            testRollBackTransaction(studentService, studentsCoursesService);
        };
    }

    private List<Student> findAllStudentsWithCourses(StudentService studentService, StudentsCoursesService studentsCoursesService) {
        List<Student> students = studentService.findAll();
        for (Student student : students) {
            student.setCoursesId(studentsCoursesService.findByIdStudent(student.getId()));
        }

        return students;
    }

    private void CRUDCourse(CourseService courseService) {
        Course course = new Course(1, "java", "krasivo");
        courseService.save(course);
        System.out.println("SAVE {COURSES}: " + course);
        System.out.println("FIND ALL {COURSES}: " + courseService.findAll());

        Course newCourse = courseService.findById(1);
        System.out.println("GET {COURSES}: " + newCourse);
        System.out.println("FIND ALL {COURSES}: " + courseService.findAll());

        newCourse.setName("C#");
        newCourse.setDescription("ne ochen");
        courseService.update(newCourse);
        System.out.println("UPDATE {COURSES}: " + newCourse);
        System.out.println("FIND ALL {COURSES}: " + courseService.findAll());

        courseService.delete(1);
        System.out.println("DELETE {COURSES}: " + newCourse);
        System.out.println("FIND ALL {COURSES}: " + courseService.findAll());
    }

    private void CRUDStudent(StudentService studentService, CourseService courseService,
                             StudentsCoursesService studentsCoursesService) {
        Course java = new Course(2, "java PRO", "krasivo ochen");
        courseService.save(java);

        Student john = new Student(1, "John", 27, new ArrayList<>(List.of(2)));
        try {
            studentsCoursesService.save(john);
        } catch (Exception e) {
            System.out.println("Транзакция отменена!");
        }
        System.out.println("SAVE {STUDENTS}: " + john);
        System.out.println("SAVE {STUDENTS COURSES}: " + john);
        System.out.println("FIND {STUDENTS COURSES}: " + studentsCoursesService.findByIdStudent(1));
        System.out.println("FIND ALL {STUDENTS}: " + findAllStudentsWithCourses(studentService, studentsCoursesService));

        Student newJohn = new Student();
        try {
            newJohn = studentsCoursesService.find(1);
        } catch (Exception e) {
            System.out.println("Транзакция отменена!");
        }
        System.out.println("GET {STUDENTS}: " + newJohn);
        System.out.println("FIND ALL {STUDENTS}: " + findAllStudentsWithCourses(studentService, studentsCoursesService));

        newJohn.setAge(21);
        try {
            studentsCoursesService.update(newJohn);
        } catch (Exception e) {
            System.out.println("Транзакция отменена!");
        }
        System.out.println("UPDATE {STUDENTS}: " + newJohn);
        System.out.println("FIND ALL {STUDENTS}: " + findAllStudentsWithCourses(studentService, studentsCoursesService));

        System.out.println("FIND UNTIL DELETE {STUDENTS COURSES}: " + studentsCoursesService.findByIdStudent(1));
        studentService.delete(2);
        System.out.println("DELETE {STUDENTS}: " + newJohn);
        System.out.println("FIND {STUDENTS COURSES}: " + studentsCoursesService.findByIdStudent(1));
        System.out.println("FIND ALL {STUDENTS}: " + findAllStudentsWithCourses(studentService, studentsCoursesService));
    }

    private void findTopAverageAgeAmongStudents(StudentService studentService, CourseService courseService,
                                                StudentsCoursesService studentsCoursesService) {
        Course course1 = new Course(3, "c++", "poidet");
        Course course2 = new Course(4, "c#", "very bad");
        courseService.save(course1);
        courseService.save(course2);


        Student student = new Student(2, "Petya", 17, new ArrayList<>(List.of(4, 2)));
        Student student1 = new Student(3, "Vasya", 27, new ArrayList<>(List.of(3, 2)));
        Student student2 = new Student(4, "Kirill", 29, new ArrayList<>(List.of(3)));
        Student student3 = new Student(5, "Maks", 37, new ArrayList<>(List.of(2)));
        try {
            studentsCoursesService.save(student);
            studentsCoursesService.save(student1);
            studentsCoursesService.save(student2);
            studentsCoursesService.save(student3);
        } catch (Exception e) {
            System.out.println("Транзакция отменена!");
        }

        System.out.println("FIND ALL {STUDENTS}: " + findAllStudentsWithCourses(studentService, studentsCoursesService));
        System.out.println("TOP COURSE: " + courseService.findTopAvgAgeAmongStudents());
    }

    private void updateStudent(StudentsCoursesService studentsCoursesService) {
        Student student = new Student(6, "new student", 24, new ArrayList<>(List.of(2, 3)));
        try {
            studentsCoursesService.save(student);
        } catch (Exception e) {
            System.out.println("Транзакция отменена!");
        }
        System.out.println("SAVE {STUDENT}: " + student);

        student.setAge(17);
        student.setName("no name");
        student.getCoursesId().remove(1);
        student.getCoursesId().add(4);

        try {
            studentsCoursesService.update(student);
        } catch (Exception e) {
            System.out.println("Транзакция отменена!");
        }
        System.out.println("UPDATE {STUDENT}: " + student);
        System.out.println("FIND {STUDENTS COURSES}: " + studentsCoursesService.findByIdStudent(6));
    }

    private void testRollBackTransaction(StudentService studentService, StudentsCoursesService studentsCoursesService) {
        try {
            Student student = new Student(7, "not save student", 228, new ArrayList<>(List.of(1, 2)));
            studentsCoursesService.save(student);
        } catch (Exception e) {
            System.out.println("Транзакция отменена!");
        }
        System.out.println("FIND ALL {STUDENTS}: " + findAllStudentsWithCourses(studentService, studentsCoursesService));
    }
}
