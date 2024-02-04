package ru.tkachenko.springbooking.service;

import ru.tkachenko.springbooking.model.RoleType;
import ru.tkachenko.springbooking.model.User;

import java.util.List;

public interface UserService {
    User findByName(String name);

    List<User> findAll();

    User findById(Long id);

    User save(User user, RoleType role);

    User update(User user);

    void deleteById(Long id);
}
