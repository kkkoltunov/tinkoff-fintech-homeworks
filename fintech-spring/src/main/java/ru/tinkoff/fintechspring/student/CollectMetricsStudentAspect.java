package ru.tinkoff.fintechspring.student;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class CollectMetricsStudentAspect {
    private final Map<Student, Integer> busyStudentsMap = new HashMap<>();

    @Around("@annotation(CountStudents)")
    public void collectMetricsStudent(ProceedingJoinPoint joinPoint) throws Throwable {
        List<Student> studentList = (List<Student>) joinPoint.proceed();
        studentList.forEach(i -> busyStudentsMap.put(i, busyStudentsMap.getOrDefault(i, 0) + 1));
        System.out.println(busyStudentsMap);
    }
}
