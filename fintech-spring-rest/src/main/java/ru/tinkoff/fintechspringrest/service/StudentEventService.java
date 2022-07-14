package ru.tinkoff.fintechspringrest.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintechspringrest.dao.StudentEventRepository;
import ru.tinkoff.fintechspringrest.event.StudentEvent;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentEventService {

    private final StudentEventRepository studentEventRepository;

    public void save(StudentEvent studentEvent) {
        studentEventRepository.save(studentEvent);
    }

    public List<StudentEvent> findAll() {
        return studentEventRepository.findAll();
    }

    public void deleteByListId(List<UUID> uuidList) {
        studentEventRepository.deleteByListId(uuidList);
    }
}
