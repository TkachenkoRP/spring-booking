package ru.tkachenko.springbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springbooking.exception.EntityNotFoundException;
import ru.tkachenko.springbooking.exception.UserRegistrationException;
import ru.tkachenko.springbooking.model.RoleType;
import ru.tkachenko.springbooking.model.User;
import ru.tkachenko.springbooking.repository.UserRepository;
import ru.tkachenko.springbooking.service.UserService;
import ru.tkachenko.springbooking.utils.BeanUtils;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseUserService implements UserService {
    private final UserRepository repository;

    @Override
    public User findByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Пользователь с именем {0} не найден!", name
                )));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Пользователь с ID {0} не найден!", id
                )));
    }

    @Override
    public User save(User user, RoleType role) {
        if (repository.existsByNameOrEmail(user.getName(), user.getEmail())) {
            throw new UserRegistrationException(MessageFormat.format(
                    "Пользователь с именем {0} и/или электронной почтой {1} уже зарегистрирован!",
                    user.getName(), user.getEmail())
            );
        }
        user.setRole(role);
        return repository.save(user);
    }

    @Override
    public User update(User user) {
        User existedUser = findById(user.getId());
        BeanUtils.copyNonNullProperties(user, existedUser);
        return repository.save(existedUser);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
