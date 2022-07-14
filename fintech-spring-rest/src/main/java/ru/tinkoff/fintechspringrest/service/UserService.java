package ru.tinkoff.fintechspringrest.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tinkoff.fintechspringrest.dao.RoleRepository;
import ru.tinkoff.fintechspringrest.dao.UserRepository;
import ru.tinkoff.fintechspringrest.model.Role;
import ru.tinkoff.fintechspringrest.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UsersRolesService usersRolesService;

    public void save(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with name = " + username + " not found!"));

        List<Integer> listRoles = usersRolesService.find(user.getId());
        List<Role> roles = roleRepository.findAll();

        List<Role> actualRoles = new ArrayList<>();
        for (int roleId : listRoles) {
            for (Role role : roles) {
                if (roleId == role.getId()) {
                    actualRoles.add(role);
                    break;
                }
            }
        }

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                mapRolesToAuthorities(actualRoles));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(int id) {
        userRepository.delete(id);
    }
}
