package ru.tinkoff.fintechspringrest.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.fintechspringrest.dao.StudentRepository;
import ru.tinkoff.fintechspringrest.event.Status;
import ru.tinkoff.fintechspringrest.event.StudentEvent;
import ru.tinkoff.fintechspringrest.model.Student;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    private final StudentEventService studentEventService;

    @Transactional
    public void save(Student student) {
        studentRepository.save(student);
        studentEventService.save(new StudentEvent(UUID.randomUUID(), student.getId(), Status.CREATED));
    }

    public Student findById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with id = " + id + " not found!"));
    }

    public Student getById(int id) {
        return findById(id);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Transactional
    public void update(Student student) {
        studentRepository.update(student);
        studentEventService.save(new StudentEvent(UUID.randomUUID(), student.getId(), Status.UPDATED));
    }

    @Transactional
    public void delete(int id) {
        studentRepository.delete(id);
        studentEventService.save(new StudentEvent(UUID.randomUUID(), id, Status.DELETED));
    }
}