package ru.tkachenko.springbooking.service;

import ru.tkachenko.springbooking.model.User;

import java.util.List;

public interface UserService {
    User findByName(String name);

    List<User> findAll();

    User findById(Long id);

    User save(User user, String role);

    User update(User user);

    void deleteById(Long id);
}
