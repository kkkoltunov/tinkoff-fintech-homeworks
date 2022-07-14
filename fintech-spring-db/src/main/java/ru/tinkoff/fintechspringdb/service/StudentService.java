package ru.tinkoff.fintechspringdb.service;

import org.springframework.stereotype.Service;
import ru.tinkoff.fintechspringdb.dao.StudentRepository;
import ru.tinkoff.fintechspringdb.model.Student;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public void save(Student student) {
        repository.save(student);
    }

    public Student findById(int id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public void update(Student student) {
        repository.update(student);
    }

    public void delete(int id) {
        repository.delete(id);
    }
}