package ru.tkachenko.springbooking.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.tkachenko.springbooking.dto.UserRegisteredEvent;

public interface UserRegisteredEventRepository extends MongoRepository<UserRegisteredEvent, String> {
}
