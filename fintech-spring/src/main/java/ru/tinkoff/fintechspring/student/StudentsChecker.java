package ru.tinkoff.fintechspring.student;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Component
public class StudentsChecker {
    private final List<Student> students = new ArrayList<>();

    @Scheduled(cron = "0 0 * * * *")
    @CountStudents
    public List<Student> getBusyStudents() {
        List<Student> busyStudentsList = students.stream()
                .filter(student -> student.getLessonsStart().compareTo(LocalTime.now()) <= 0 &&
                        student.getLessonsEnd().compareTo(LocalTime.now()) >= 0)
                .toList();

        System.out.println(busyStudentsList);
        return busyStudentsList;
    }

    @PostConstruct
    private void loadStudentData(){
        List<String> fileLines;
        try {
            fileLines = Files.readAllLines(Paths.get("students.csv"));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось получить информацию из файла!");
        }
        fileLines.remove(0);

        for (String student : fileLines) {
            String[] studentSplit = student.split(",");
            students.add(Student.of(
                    studentSplit[0].substring(1, studentSplit[0].length() - 1).trim(),
                    Integer.parseInt(studentSplit[1]),
                    studentSplit[2],
                    studentSplit[3]
            ));
        }
    }
}