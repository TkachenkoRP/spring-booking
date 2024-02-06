package ru.tkachenko.springbooking.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tkachenko.springbooking.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByName(String name);

    boolean existsByNameOrEmail(String name, String email);

    boolean existsByNameAndIdNotOrEmailAndIdNot(String name, Long id, String email, Long idIsTheSame);
}
