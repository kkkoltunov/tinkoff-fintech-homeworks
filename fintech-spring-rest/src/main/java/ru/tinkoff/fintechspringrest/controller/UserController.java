package ru.tinkoff.fintechspringrest.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintechspringrest.model.Student;
import ru.tinkoff.fintechspringrest.model.User;
import ru.tinkoff.fintechspringrest.service.UserService;
import ru.tinkoff.fintechspringrest.service.UsersRolesService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UsersRolesService usersRolesService;

    @PostMapping
    public void createUser(@Valid @RequestBody User user) {
        userService.save(user);
        usersRolesService.save(Pair.of(user.getId(), 2));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteUsersById(@PathVariable("id") int id) {
        userService.delete(id);
    }

}
