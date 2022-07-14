package ru.tinkoff.fintechspringrest.service;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintechspringrest.dao.UsersRolesRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UsersRolesService {

    private final UsersRolesRepository usersRolesRepository;

    public void save(Pair<Integer, Integer> pair) {
        usersRolesRepository.save(pair.getFirst(), pair.getSecond());
    }

    public List<Integer> find(int userId) {
        return usersRolesRepository.find(userId);
    }
}
